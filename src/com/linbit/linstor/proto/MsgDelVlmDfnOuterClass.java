// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: linstor/proto/MsgDelVlmDfn.proto

package com.linbit.linstor.proto;

public final class MsgDelVlmDfnOuterClass {
  private MsgDelVlmDfnOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface MsgDelVlmDfnOrBuilder extends
      // @@protoc_insertion_point(interface_extends:com.linbit.linstor.proto.MsgDelVlmDfn)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string vlm_dfn_uuid = 1;</code>
     */
    boolean hasVlmDfnUuid();
    /**
     * <code>optional string vlm_dfn_uuid = 1;</code>
     */
    java.lang.String getVlmDfnUuid();
    /**
     * <code>optional string vlm_dfn_uuid = 1;</code>
     */
    com.google.protobuf.ByteString
        getVlmDfnUuidBytes();

    /**
     * <pre>
     * Resource name
     * </pre>
     *
     * <code>required string rsc_name = 2;</code>
     */
    boolean hasRscName();
    /**
     * <pre>
     * Resource name
     * </pre>
     *
     * <code>required string rsc_name = 2;</code>
     */
    java.lang.String getRscName();
    /**
     * <pre>
     * Resource name
     * </pre>
     *
     * <code>required string rsc_name = 2;</code>
     */
    com.google.protobuf.ByteString
        getRscNameBytes();

    /**
     * <pre>
     * VolumeNr
     * </pre>
     *
     * <code>required sint32 vlm_nr = 3;</code>
     */
    boolean hasVlmNr();
    /**
     * <pre>
     * VolumeNr
     * </pre>
     *
     * <code>required sint32 vlm_nr = 3;</code>
     */
    int getVlmNr();
  }
  /**
   * <pre>
   * linstor - Delete volume definition
   * </pre>
   *
   * Protobuf type {@code com.linbit.linstor.proto.MsgDelVlmDfn}
   */
  public  static final class MsgDelVlmDfn extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:com.linbit.linstor.proto.MsgDelVlmDfn)
      MsgDelVlmDfnOrBuilder {
    // Use MsgDelVlmDfn.newBuilder() to construct.
    private MsgDelVlmDfn(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private MsgDelVlmDfn() {
      vlmDfnUuid_ = "";
      rscName_ = "";
      vlmNr_ = 0;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private MsgDelVlmDfn(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000001;
              vlmDfnUuid_ = bs;
              break;
            }
            case 18: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000002;
              rscName_ = bs;
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              vlmNr_ = input.readSInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.internal_static_com_linbit_linstor_proto_MsgDelVlmDfn_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.internal_static_com_linbit_linstor_proto_MsgDelVlmDfn_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn.class, com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn.Builder.class);
    }

    private int bitField0_;
    public static final int VLM_DFN_UUID_FIELD_NUMBER = 1;
    private volatile java.lang.Object vlmDfnUuid_;
    /**
     * <code>optional string vlm_dfn_uuid = 1;</code>
     */
    public boolean hasVlmDfnUuid() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional string vlm_dfn_uuid = 1;</code>
     */
    public java.lang.String getVlmDfnUuid() {
      java.lang.Object ref = vlmDfnUuid_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          vlmDfnUuid_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string vlm_dfn_uuid = 1;</code>
     */
    public com.google.protobuf.ByteString
        getVlmDfnUuidBytes() {
      java.lang.Object ref = vlmDfnUuid_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        vlmDfnUuid_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int RSC_NAME_FIELD_NUMBER = 2;
    private volatile java.lang.Object rscName_;
    /**
     * <pre>
     * Resource name
     * </pre>
     *
     * <code>required string rsc_name = 2;</code>
     */
    public boolean hasRscName() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <pre>
     * Resource name
     * </pre>
     *
     * <code>required string rsc_name = 2;</code>
     */
    public java.lang.String getRscName() {
      java.lang.Object ref = rscName_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          rscName_ = s;
        }
        return s;
      }
    }
    /**
     * <pre>
     * Resource name
     * </pre>
     *
     * <code>required string rsc_name = 2;</code>
     */
    public com.google.protobuf.ByteString
        getRscNameBytes() {
      java.lang.Object ref = rscName_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        rscName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int VLM_NR_FIELD_NUMBER = 3;
    private int vlmNr_;
    /**
     * <pre>
     * VolumeNr
     * </pre>
     *
     * <code>required sint32 vlm_nr = 3;</code>
     */
    public boolean hasVlmNr() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <pre>
     * VolumeNr
     * </pre>
     *
     * <code>required sint32 vlm_nr = 3;</code>
     */
    public int getVlmNr() {
      return vlmNr_;
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasRscName()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasVlmNr()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, vlmDfnUuid_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, rscName_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeSInt32(3, vlmNr_);
      }
      unknownFields.writeTo(output);
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, vlmDfnUuid_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, rscName_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeSInt32Size(3, vlmNr_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn)) {
        return super.equals(obj);
      }
      com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn other = (com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn) obj;

      boolean result = true;
      result = result && (hasVlmDfnUuid() == other.hasVlmDfnUuid());
      if (hasVlmDfnUuid()) {
        result = result && getVlmDfnUuid()
            .equals(other.getVlmDfnUuid());
      }
      result = result && (hasRscName() == other.hasRscName());
      if (hasRscName()) {
        result = result && getRscName()
            .equals(other.getRscName());
      }
      result = result && (hasVlmNr() == other.hasVlmNr());
      if (hasVlmNr()) {
        result = result && (getVlmNr()
            == other.getVlmNr());
      }
      result = result && unknownFields.equals(other.unknownFields);
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (hasVlmDfnUuid()) {
        hash = (37 * hash) + VLM_DFN_UUID_FIELD_NUMBER;
        hash = (53 * hash) + getVlmDfnUuid().hashCode();
      }
      if (hasRscName()) {
        hash = (37 * hash) + RSC_NAME_FIELD_NUMBER;
        hash = (53 * hash) + getRscName().hashCode();
      }
      if (hasVlmNr()) {
        hash = (37 * hash) + VLM_NR_FIELD_NUMBER;
        hash = (53 * hash) + getVlmNr();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * <pre>
     * linstor - Delete volume definition
     * </pre>
     *
     * Protobuf type {@code com.linbit.linstor.proto.MsgDelVlmDfn}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:com.linbit.linstor.proto.MsgDelVlmDfn)
        com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfnOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.internal_static_com_linbit_linstor_proto_MsgDelVlmDfn_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.internal_static_com_linbit_linstor_proto_MsgDelVlmDfn_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn.class, com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn.Builder.class);
      }

      // Construct using com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        vlmDfnUuid_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        rscName_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        vlmNr_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.internal_static_com_linbit_linstor_proto_MsgDelVlmDfn_descriptor;
      }

      public com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn getDefaultInstanceForType() {
        return com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn.getDefaultInstance();
      }

      public com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn build() {
        com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn buildPartial() {
        com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn result = new com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.vlmDfnUuid_ = vlmDfnUuid_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.rscName_ = rscName_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.vlmNr_ = vlmNr_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn) {
          return mergeFrom((com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn other) {
        if (other == com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn.getDefaultInstance()) return this;
        if (other.hasVlmDfnUuid()) {
          bitField0_ |= 0x00000001;
          vlmDfnUuid_ = other.vlmDfnUuid_;
          onChanged();
        }
        if (other.hasRscName()) {
          bitField0_ |= 0x00000002;
          rscName_ = other.rscName_;
          onChanged();
        }
        if (other.hasVlmNr()) {
          setVlmNr(other.getVlmNr());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        if (!hasRscName()) {
          return false;
        }
        if (!hasVlmNr()) {
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object vlmDfnUuid_ = "";
      /**
       * <code>optional string vlm_dfn_uuid = 1;</code>
       */
      public boolean hasVlmDfnUuid() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional string vlm_dfn_uuid = 1;</code>
       */
      public java.lang.String getVlmDfnUuid() {
        java.lang.Object ref = vlmDfnUuid_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            vlmDfnUuid_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string vlm_dfn_uuid = 1;</code>
       */
      public com.google.protobuf.ByteString
          getVlmDfnUuidBytes() {
        java.lang.Object ref = vlmDfnUuid_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          vlmDfnUuid_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string vlm_dfn_uuid = 1;</code>
       */
      public Builder setVlmDfnUuid(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        vlmDfnUuid_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string vlm_dfn_uuid = 1;</code>
       */
      public Builder clearVlmDfnUuid() {
        bitField0_ = (bitField0_ & ~0x00000001);
        vlmDfnUuid_ = getDefaultInstance().getVlmDfnUuid();
        onChanged();
        return this;
      }
      /**
       * <code>optional string vlm_dfn_uuid = 1;</code>
       */
      public Builder setVlmDfnUuidBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        vlmDfnUuid_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object rscName_ = "";
      /**
       * <pre>
       * Resource name
       * </pre>
       *
       * <code>required string rsc_name = 2;</code>
       */
      public boolean hasRscName() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <pre>
       * Resource name
       * </pre>
       *
       * <code>required string rsc_name = 2;</code>
       */
      public java.lang.String getRscName() {
        java.lang.Object ref = rscName_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            rscName_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <pre>
       * Resource name
       * </pre>
       *
       * <code>required string rsc_name = 2;</code>
       */
      public com.google.protobuf.ByteString
          getRscNameBytes() {
        java.lang.Object ref = rscName_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          rscName_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       * Resource name
       * </pre>
       *
       * <code>required string rsc_name = 2;</code>
       */
      public Builder setRscName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        rscName_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Resource name
       * </pre>
       *
       * <code>required string rsc_name = 2;</code>
       */
      public Builder clearRscName() {
        bitField0_ = (bitField0_ & ~0x00000002);
        rscName_ = getDefaultInstance().getRscName();
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Resource name
       * </pre>
       *
       * <code>required string rsc_name = 2;</code>
       */
      public Builder setRscNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        rscName_ = value;
        onChanged();
        return this;
      }

      private int vlmNr_ ;
      /**
       * <pre>
       * VolumeNr
       * </pre>
       *
       * <code>required sint32 vlm_nr = 3;</code>
       */
      public boolean hasVlmNr() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <pre>
       * VolumeNr
       * </pre>
       *
       * <code>required sint32 vlm_nr = 3;</code>
       */
      public int getVlmNr() {
        return vlmNr_;
      }
      /**
       * <pre>
       * VolumeNr
       * </pre>
       *
       * <code>required sint32 vlm_nr = 3;</code>
       */
      public Builder setVlmNr(int value) {
        bitField0_ |= 0x00000004;
        vlmNr_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * VolumeNr
       * </pre>
       *
       * <code>required sint32 vlm_nr = 3;</code>
       */
      public Builder clearVlmNr() {
        bitField0_ = (bitField0_ & ~0x00000004);
        vlmNr_ = 0;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:com.linbit.linstor.proto.MsgDelVlmDfn)
    }

    // @@protoc_insertion_point(class_scope:com.linbit.linstor.proto.MsgDelVlmDfn)
    private static final com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn();
    }

    public static com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    @java.lang.Deprecated public static final com.google.protobuf.Parser<MsgDelVlmDfn>
        PARSER = new com.google.protobuf.AbstractParser<MsgDelVlmDfn>() {
      public MsgDelVlmDfn parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new MsgDelVlmDfn(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<MsgDelVlmDfn> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<MsgDelVlmDfn> getParserForType() {
      return PARSER;
    }

    public com.linbit.linstor.proto.MsgDelVlmDfnOuterClass.MsgDelVlmDfn getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_linbit_linstor_proto_MsgDelVlmDfn_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_linbit_linstor_proto_MsgDelVlmDfn_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n linstor/proto/MsgDelVlmDfn.proto\022\030com." +
      "linbit.linstor.proto\032#linstor/proto/LinS" +
      "torMapEntry.proto\032\032linstor/proto/VlmDfn." +
      "proto\"F\n\014MsgDelVlmDfn\022\024\n\014vlm_dfn_uuid\030\001 " +
      "\001(\t\022\020\n\010rsc_name\030\002 \002(\t\022\016\n\006vlm_nr\030\003 \002(\021P\000P" +
      "\001"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.linbit.linstor.proto.LinStorMapEntryOuterClass.getDescriptor(),
          com.linbit.linstor.proto.VlmDfnOuterClass.getDescriptor(),
        }, assigner);
    internal_static_com_linbit_linstor_proto_MsgDelVlmDfn_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_linbit_linstor_proto_MsgDelVlmDfn_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_linbit_linstor_proto_MsgDelVlmDfn_descriptor,
        new java.lang.String[] { "VlmDfnUuid", "RscName", "VlmNr", });
    com.linbit.linstor.proto.LinStorMapEntryOuterClass.getDescriptor();
    com.linbit.linstor.proto.VlmDfnOuterClass.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
