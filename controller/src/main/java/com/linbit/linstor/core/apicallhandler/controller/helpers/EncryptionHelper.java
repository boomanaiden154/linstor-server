package com.linbit.linstor.core.apicallhandler.controller.helpers;

import com.linbit.ImplementationError;
import com.linbit.crypto.LengthPadding;
import com.linbit.crypto.SymmetricKeyCipher;
import com.linbit.crypto.SymmetricKeyCipher.CipherStrength;
import com.linbit.linstor.InternalApiConsts;
import com.linbit.linstor.LinStorException;
import com.linbit.linstor.annotation.ApiContext;
import com.linbit.linstor.api.ApiCallRcImpl;
import com.linbit.linstor.api.ApiConsts;
import com.linbit.linstor.api.interfaces.serializer.CtrlStltSerializer;
import com.linbit.linstor.core.CoreModule.NodesMap;
import com.linbit.linstor.core.CtrlSecurityObjects;
import com.linbit.linstor.core.SecretGenerator;
import com.linbit.linstor.core.apicallhandler.controller.exceptions.IncorrectPassphraseException;
import com.linbit.linstor.core.apicallhandler.controller.exceptions.MissingKeyPropertyException;
import com.linbit.linstor.core.apicallhandler.response.ApiRcException;
import com.linbit.linstor.core.objects.Node;
import com.linbit.linstor.core.repository.SystemConfRepository;
import com.linbit.linstor.dbdrivers.DatabaseException;
import com.linbit.linstor.netcom.Peer;
import com.linbit.linstor.propscon.InvalidKeyException;
import com.linbit.linstor.propscon.InvalidValueException;
import com.linbit.linstor.propscon.Props;
import com.linbit.linstor.security.AccessContext;
import com.linbit.linstor.security.AccessDeniedException;
import com.linbit.linstor.transaction.manager.TransactionMgr;
import com.linbit.utils.Base64;

import javax.inject.Singleton;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.google.inject.Inject;
import com.google.inject.Provider;

@Singleton
public class EncryptionHelper
{
    public static final String NAMESPACE_ENCRYPTED = "encrypted";
    public static final String KEY_CRYPT_HASH = "masterhash";
    public static final String KEY_CRYPT_KEY = "masterkey";
    public static final String KEY_PASSPHRASE_SALT = "passphrasesalt";
    private static final int MASTER_KEY_BYTES = 16; // TODO make configurable
    private static final int MASTER_KEY_SALT_BYTES = 16; // TODO make configurable

    private static MessageDigest sha512;

    private final SystemConfRepository systemConfRepository;
    private final LengthPadding cryptoLenPad;
    private final Provider<TransactionMgr> transMgrProvider;
    private final CtrlSecurityObjects ctrlSecObj;
    private final CtrlStltSerializer ctrlStltSrzl;
    private final NodesMap nodesMap;
    private final AccessContext apiCtx;

    static
    {
        try
        {
            sha512 = MessageDigest.getInstance("SHA-512");
        }
        catch (NoSuchAlgorithmException exc)
        {
            throw new ImplementationError(exc);
        }
    }

    @Inject
    public EncryptionHelper(
        SystemConfRepository systemConfRepositoryRef,
        LengthPadding cryptoLenPadRef,
        Provider<TransactionMgr> transMgrProviderRef,
        CtrlSecurityObjects ctrlSecObjRef,
        CtrlStltSerializer ctrlStltSrzlRef,
        NodesMap nodesMapRef,
        @ApiContext
        AccessContext apiCtxRef
    )
    {
        systemConfRepository = systemConfRepositoryRef;
        cryptoLenPad = cryptoLenPadRef;
        transMgrProvider = transMgrProviderRef;
        ctrlSecObj = ctrlSecObjRef;
        ctrlStltSrzl = ctrlStltSrzlRef;
        nodesMap = nodesMapRef;
        apiCtx = apiCtxRef;
    }

    public Props getEncryptedNamespace(AccessContext peerAccCtxRef) throws AccessDeniedException
    {
        return systemConfRepository.getCtrlConfForView(peerAccCtxRef).getNamespace(NAMESPACE_ENCRYPTED)
            .orElse(null);
    }

    public byte[] generateSecret()
    {
        return SecretGenerator.generateSecret(MASTER_KEY_BYTES);
    }

    public boolean passphraseExists(AccessContext peerAccCtxRef) throws AccessDeniedException
    {
        Props namespace = getEncryptedNamespace(peerAccCtxRef);

        boolean exists = false;
        if (namespace != null)
        {
            String masterHashStr = namespace.getProp(KEY_CRYPT_HASH);
            String encryptedMasterKeyStr = namespace.getProp(KEY_CRYPT_KEY);
            String passphraseSaltStr = namespace.getProp(KEY_PASSPHRASE_SALT);

            exists = masterHashStr != null &&
                encryptedMasterKeyStr != null &&
                passphraseSaltStr != null;
        }
        return exists;
    }

    public void setPassphraseImpl(String newPassphrase, byte[] masterKey, AccessContext peerAccCtxRef)
        throws InvalidKeyException, InvalidValueException, AccessDeniedException, DatabaseException, LinStorException
    {
        Props ctrlConf = systemConfRepository.getCtrlConfForChange(peerAccCtxRef);

        // Add length padding to the master key, encrypt with the new passphrase and a generated salt,
        // and store the encrypted key, the salt and a hash of the length padded key in the database
        byte[] salt = SecretGenerator.generateSecret(MASTER_KEY_SALT_BYTES);
        SymmetricKeyCipher cipher = SymmetricKeyCipher.getInstanceWithPassword(
            salt,
            newPassphrase.getBytes(StandardCharsets.UTF_8),
            CipherStrength.KEY_LENGTH_128 // TODO if MASTER_KEY_BYTES is configurable, this also has to be configurable
        );

        byte[] encodedData = cryptoLenPad.conceal(masterKey);
        // Store a hash of the length padded key in the database
        sha512.reset();
        byte[] hash = sha512.digest(encodedData);
        ctrlConf.setProp(KEY_CRYPT_HASH, Base64.encode(hash), NAMESPACE_ENCRYPTED);
        byte[] encryptedMasterKey = cipher.encrypt(encodedData);

        ctrlConf.setProp(KEY_CRYPT_KEY, Base64.encode(encryptedMasterKey), NAMESPACE_ENCRYPTED);
        ctrlConf.setProp(KEY_PASSPHRASE_SALT, Base64.encode(salt), NAMESPACE_ENCRYPTED);

        transMgrProvider.get().commit();
    }

    public byte[] getDecryptedMasterKey(Props namespace, String oldPassphrase)
        throws InvalidKeyException, LinStorException
    {
        return getDecryptedMasterKey(
            namespace.getProp(KEY_CRYPT_HASH),
            namespace.getProp(KEY_CRYPT_KEY),
            namespace.getProp(KEY_PASSPHRASE_SALT),
            oldPassphrase
        );
    }

    public byte[] getDecryptedMasterKey(
        String masterHashBase64,
        String encKeyBase64,
        String passSaltBase64,
        String passphraseUtf8
    )
        throws LinStorException
    {

        if (masterHashBase64 == null || encKeyBase64 == null || passSaltBase64 == null)
        {
            throw new MissingKeyPropertyException("Could not restore crypt passphrase as a property is not set");
        }
        else
        {
            return getDecryptedMasterKey(
                Base64.decode(masterHashBase64),
                Base64.decode(encKeyBase64),
                Base64.decode(passSaltBase64),
                passphraseUtf8.getBytes(StandardCharsets.UTF_8)
            );
        }
    }

    public byte[] getDecryptedMasterKey(
        byte[] masterHash,
        byte[] encryptedMasterKey,
        byte[] passphraseSalt,
        byte[] passphrase
    )
        throws LinStorException
    {
        byte[] ret = null;
        SymmetricKeyCipher ciper = SymmetricKeyCipher.getInstanceWithPassword(
            passphraseSalt,
            passphrase,
            CipherStrength.KEY_LENGTH_128
        );
        // TODO: if MASTER_KEY_BYTES is configurable, the CipherStrength also has to be configurable

        byte[] decryptedData = ciper.decrypt(encryptedMasterKey);

        sha512.reset();
        byte[] hashedMasterKey = sha512.digest(decryptedData);

        if (Arrays.equals(hashedMasterKey, masterHash))
        {
            ret = cryptoLenPad.retrieve(decryptedData);
        }
        else
        {
            throw new IncorrectPassphraseException(
                "Could not restore master passphrase as the given old passphrase was incorrect"
            );
        }
        return ret;
    }

    public void setCryptKey(byte[] cryptKey, Props namespace, boolean updateSatellites)
    {
        byte[] cryptHash = Base64.decode(namespace.getProp(KEY_CRYPT_HASH));
        byte[] cryptSalt = Base64.decode(namespace.getProp(KEY_PASSPHRASE_SALT));
        byte[] encKey = Base64.decode(namespace.getProp(KEY_CRYPT_KEY));
        ctrlSecObj.setCryptKey(cryptKey, cryptHash, cryptSalt, encKey);

        if (updateSatellites)
        {
            for (Node node : nodesMap.values())
            {
                Peer peer;
                try
                {
                    peer = node.getPeer(apiCtx);
                    peer.sendMessage(
                        ctrlStltSrzl.onewayBuilder(InternalApiConsts.API_CRYPT_KEY)
                            .cryptKey(
                                cryptKey,
                                cryptHash,
                                cryptSalt,
                                encKey,
                                peer.getFullSyncId(),
                                peer.getNextSerializerId()
                            )
                            .build()
                    );
                }
                catch (AccessDeniedException exc)
                {
                    throw new ImplementationError(exc);
                }
            }
        }
    }

    public byte[] encrypt(String plainKey) throws LinStorException
    {
        return encrypt(plainKey.getBytes());
    }

    public byte[] encrypt(byte[] plainKey) throws LinStorException
    {
        byte[] masterKey = ctrlSecObj.getCryptKey();
        if (masterKey == null || masterKey.length == 0)
        {
            throw new ApiRcException(
                ApiCallRcImpl
                    .entryBuilder(
                        ApiConsts.FAIL_NOT_FOUND_CRYPT_KEY,
                        "Unable to create encrypt key without having a master key"
                    )
                    .setCause("The masterkey was not initialized yet")
                    .setCorrection("Create or enter the master passphrase")
                    .build()
            );
        }
        return encrypt(masterKey, plainKey);
    }

    public byte[] encrypt(byte[] key, byte[] toEncrypt) throws LinStorException
    {
        SymmetricKeyCipher cipher = SymmetricKeyCipher.getInstanceWithKey(key);

        byte[] encodedData = cryptoLenPad.conceal(toEncrypt);
        return cipher.encrypt(encodedData);
    }
}
