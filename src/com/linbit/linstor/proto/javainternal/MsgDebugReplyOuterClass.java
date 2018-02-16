// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: linstor/proto/javainternal/MsgDebugReply.proto

package com.linbit.linstor.proto.javainternal;

public final class MsgDebugReplyOuterClass {
  private MsgDebugReplyOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface MsgDebugReplyOrBuilder extends
      // @@protoc_insertion_point(interface_extends:com.linbit.linstor.proto.javainternal.MsgDebugReply)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * Debug command standard output
     * </pre>
     *
     * <code>repeated string debug_out = 1;</code>
     */
    java.util.List<java.lang.String>
        getDebugOutList();
    /**
     * <pre>
     * Debug command standard output
     * </pre>
     *
     * <code>repeated string debug_out = 1;</code>
     */
    int getDebugOutCount();
    /**
     * <pre>
     * Debug command standard output
     * </pre>
     *
     * <code>repeated string debug_out = 1;</code>
     */
    java.lang.String getDebugOut(int index);
    /**
     * <pre>
     * Debug command standard output
     * </pre>
     *
     * <code>repeated string debug_out = 1;</code>
     */
    com.google.protobuf.ByteString
        getDebugOutBytes(int index);

    /**
     * <pre>
     * Debug command error output
     * </pre>
     *
     * <code>repeated string debug_err = 2;</code>
     */
    java.util.List<java.lang.String>
        getDebugErrList();
    /**
     * <pre>
     * Debug command error output
     * </pre>
     *
     * <code>repeated string debug_err = 2;</code>
     */
    int getDebugErrCount();
    /**
     * <pre>
     * Debug command error output
     * </pre>
     *
     * <code>repeated string debug_err = 2;</code>
     */
    java.lang.String getDebugErr(int index);
    /**
     * <pre>
     * Debug command error output
     * </pre>
     *
     * <code>repeated string debug_err = 2;</code>
     */
    com.google.protobuf.ByteString
        getDebugErrBytes(int index);
  }
  /**
   * <pre>
   * Debug command reply
   * </pre>
   *
   * Protobuf type {@code com.linbit.linstor.proto.javainternal.MsgDebugReply}
   */
  public  static final class MsgDebugReply extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:com.linbit.linstor.proto.javainternal.MsgDebugReply)
      MsgDebugReplyOrBuilder {
    // Use MsgDebugReply.newBuilder() to construct.
    private MsgDebugReply(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private MsgDebugReply() {
      debugOut_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      debugErr_ = com.google.protobuf.LazyStringArrayList.EMPTY;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private MsgDebugReply(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!input.skipField(tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              java.lang.String s = input.readStringRequireUtf8();
              if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
                debugOut_ = new com.google.protobuf.LazyStringArrayList();
                mutable_bitField0_ |= 0x00000001;
              }
              debugOut_.add(s);
              break;
            }
            case 18: {
              java.lang.String s = input.readStringRequireUtf8();
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
                debugErr_ = new com.google.protobuf.LazyStringArrayList();
                mutable_bitField0_ |= 0x00000002;
              }
              debugErr_.add(s);
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
        if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
          debugOut_ = debugOut_.getUnmodifiableView();
        }
        if (((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
          debugErr_ = debugErr_.getUnmodifiableView();
        }
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.internal_static_com_linbit_linstor_proto_javainternal_MsgDebugReply_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.internal_static_com_linbit_linstor_proto_javainternal_MsgDebugReply_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply.class, com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply.Builder.class);
    }

    public static final int DEBUG_OUT_FIELD_NUMBER = 1;
    private com.google.protobuf.LazyStringList debugOut_;
    /**
     * <pre>
     * Debug command standard output
     * </pre>
     *
     * <code>repeated string debug_out = 1;</code>
     */
    public com.google.protobuf.ProtocolStringList
        getDebugOutList() {
      return debugOut_;
    }
    /**
     * <pre>
     * Debug command standard output
     * </pre>
     *
     * <code>repeated string debug_out = 1;</code>
     */
    public int getDebugOutCount() {
      return debugOut_.size();
    }
    /**
     * <pre>
     * Debug command standard output
     * </pre>
     *
     * <code>repeated string debug_out = 1;</code>
     */
    public java.lang.String getDebugOut(int index) {
      return debugOut_.get(index);
    }
    /**
     * <pre>
     * Debug command standard output
     * </pre>
     *
     * <code>repeated string debug_out = 1;</code>
     */
    public com.google.protobuf.ByteString
        getDebugOutBytes(int index) {
      return debugOut_.getByteString(index);
    }

    public static final int DEBUG_ERR_FIELD_NUMBER = 2;
    private com.google.protobuf.LazyStringList debugErr_;
    /**
     * <pre>
     * Debug command error output
     * </pre>
     *
     * <code>repeated string debug_err = 2;</code>
     */
    public com.google.protobuf.ProtocolStringList
        getDebugErrList() {
      return debugErr_;
    }
    /**
     * <pre>
     * Debug command error output
     * </pre>
     *
     * <code>repeated string debug_err = 2;</code>
     */
    public int getDebugErrCount() {
      return debugErr_.size();
    }
    /**
     * <pre>
     * Debug command error output
     * </pre>
     *
     * <code>repeated string debug_err = 2;</code>
     */
    public java.lang.String getDebugErr(int index) {
      return debugErr_.get(index);
    }
    /**
     * <pre>
     * Debug command error output
     * </pre>
     *
     * <code>repeated string debug_err = 2;</code>
     */
    public com.google.protobuf.ByteString
        getDebugErrBytes(int index) {
      return debugErr_.getByteString(index);
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      for (int i = 0; i < debugOut_.size(); i++) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, debugOut_.getRaw(i));
      }
      for (int i = 0; i < debugErr_.size(); i++) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, debugErr_.getRaw(i));
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      {
        int dataSize = 0;
        for (int i = 0; i < debugOut_.size(); i++) {
          dataSize += computeStringSizeNoTag(debugOut_.getRaw(i));
        }
        size += dataSize;
        size += 1 * getDebugOutList().size();
      }
      {
        int dataSize = 0;
        for (int i = 0; i < debugErr_.size(); i++) {
          dataSize += computeStringSizeNoTag(debugErr_.getRaw(i));
        }
        size += dataSize;
        size += 1 * getDebugErrList().size();
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply)) {
        return super.equals(obj);
      }
      com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply other = (com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply) obj;

      boolean result = true;
      result = result && getDebugOutList()
          .equals(other.getDebugOutList());
      result = result && getDebugErrList()
          .equals(other.getDebugErrList());
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (getDebugOutCount() > 0) {
        hash = (37 * hash) + DEBUG_OUT_FIELD_NUMBER;
        hash = (53 * hash) + getDebugOutList().hashCode();
      }
      if (getDebugErrCount() > 0) {
        hash = (37 * hash) + DEBUG_ERR_FIELD_NUMBER;
        hash = (53 * hash) + getDebugErrList().hashCode();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply parseFrom(
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
    public static Builder newBuilder(com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply prototype) {
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
     * Debug command reply
     * </pre>
     *
     * Protobuf type {@code com.linbit.linstor.proto.javainternal.MsgDebugReply}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:com.linbit.linstor.proto.javainternal.MsgDebugReply)
        com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReplyOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.internal_static_com_linbit_linstor_proto_javainternal_MsgDebugReply_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.internal_static_com_linbit_linstor_proto_javainternal_MsgDebugReply_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply.class, com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply.Builder.class);
      }

      // Construct using com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply.newBuilder()
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
        debugOut_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        debugErr_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.internal_static_com_linbit_linstor_proto_javainternal_MsgDebugReply_descriptor;
      }

      public com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply getDefaultInstanceForType() {
        return com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply.getDefaultInstance();
      }

      public com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply build() {
        com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply buildPartial() {
        com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply result = new com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply(this);
        int from_bitField0_ = bitField0_;
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          debugOut_ = debugOut_.getUnmodifiableView();
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.debugOut_ = debugOut_;
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          debugErr_ = debugErr_.getUnmodifiableView();
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.debugErr_ = debugErr_;
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
        if (other instanceof com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply) {
          return mergeFrom((com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply other) {
        if (other == com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply.getDefaultInstance()) return this;
        if (!other.debugOut_.isEmpty()) {
          if (debugOut_.isEmpty()) {
            debugOut_ = other.debugOut_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureDebugOutIsMutable();
            debugOut_.addAll(other.debugOut_);
          }
          onChanged();
        }
        if (!other.debugErr_.isEmpty()) {
          if (debugErr_.isEmpty()) {
            debugErr_ = other.debugErr_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensureDebugErrIsMutable();
            debugErr_.addAll(other.debugErr_);
          }
          onChanged();
        }
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.google.protobuf.LazyStringList debugOut_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      private void ensureDebugOutIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          debugOut_ = new com.google.protobuf.LazyStringArrayList(debugOut_);
          bitField0_ |= 0x00000001;
         }
      }
      /**
       * <pre>
       * Debug command standard output
       * </pre>
       *
       * <code>repeated string debug_out = 1;</code>
       */
      public com.google.protobuf.ProtocolStringList
          getDebugOutList() {
        return debugOut_.getUnmodifiableView();
      }
      /**
       * <pre>
       * Debug command standard output
       * </pre>
       *
       * <code>repeated string debug_out = 1;</code>
       */
      public int getDebugOutCount() {
        return debugOut_.size();
      }
      /**
       * <pre>
       * Debug command standard output
       * </pre>
       *
       * <code>repeated string debug_out = 1;</code>
       */
      public java.lang.String getDebugOut(int index) {
        return debugOut_.get(index);
      }
      /**
       * <pre>
       * Debug command standard output
       * </pre>
       *
       * <code>repeated string debug_out = 1;</code>
       */
      public com.google.protobuf.ByteString
          getDebugOutBytes(int index) {
        return debugOut_.getByteString(index);
      }
      /**
       * <pre>
       * Debug command standard output
       * </pre>
       *
       * <code>repeated string debug_out = 1;</code>
       */
      public Builder setDebugOut(
          int index, java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureDebugOutIsMutable();
        debugOut_.set(index, value);
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Debug command standard output
       * </pre>
       *
       * <code>repeated string debug_out = 1;</code>
       */
      public Builder addDebugOut(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureDebugOutIsMutable();
        debugOut_.add(value);
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Debug command standard output
       * </pre>
       *
       * <code>repeated string debug_out = 1;</code>
       */
      public Builder addAllDebugOut(
          java.lang.Iterable<java.lang.String> values) {
        ensureDebugOutIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, debugOut_);
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Debug command standard output
       * </pre>
       *
       * <code>repeated string debug_out = 1;</code>
       */
      public Builder clearDebugOut() {
        debugOut_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Debug command standard output
       * </pre>
       *
       * <code>repeated string debug_out = 1;</code>
       */
      public Builder addDebugOutBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        ensureDebugOutIsMutable();
        debugOut_.add(value);
        onChanged();
        return this;
      }

      private com.google.protobuf.LazyStringList debugErr_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      private void ensureDebugErrIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          debugErr_ = new com.google.protobuf.LazyStringArrayList(debugErr_);
          bitField0_ |= 0x00000002;
         }
      }
      /**
       * <pre>
       * Debug command error output
       * </pre>
       *
       * <code>repeated string debug_err = 2;</code>
       */
      public com.google.protobuf.ProtocolStringList
          getDebugErrList() {
        return debugErr_.getUnmodifiableView();
      }
      /**
       * <pre>
       * Debug command error output
       * </pre>
       *
       * <code>repeated string debug_err = 2;</code>
       */
      public int getDebugErrCount() {
        return debugErr_.size();
      }
      /**
       * <pre>
       * Debug command error output
       * </pre>
       *
       * <code>repeated string debug_err = 2;</code>
       */
      public java.lang.String getDebugErr(int index) {
        return debugErr_.get(index);
      }
      /**
       * <pre>
       * Debug command error output
       * </pre>
       *
       * <code>repeated string debug_err = 2;</code>
       */
      public com.google.protobuf.ByteString
          getDebugErrBytes(int index) {
        return debugErr_.getByteString(index);
      }
      /**
       * <pre>
       * Debug command error output
       * </pre>
       *
       * <code>repeated string debug_err = 2;</code>
       */
      public Builder setDebugErr(
          int index, java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureDebugErrIsMutable();
        debugErr_.set(index, value);
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Debug command error output
       * </pre>
       *
       * <code>repeated string debug_err = 2;</code>
       */
      public Builder addDebugErr(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureDebugErrIsMutable();
        debugErr_.add(value);
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Debug command error output
       * </pre>
       *
       * <code>repeated string debug_err = 2;</code>
       */
      public Builder addAllDebugErr(
          java.lang.Iterable<java.lang.String> values) {
        ensureDebugErrIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, debugErr_);
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Debug command error output
       * </pre>
       *
       * <code>repeated string debug_err = 2;</code>
       */
      public Builder clearDebugErr() {
        debugErr_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Debug command error output
       * </pre>
       *
       * <code>repeated string debug_err = 2;</code>
       */
      public Builder addDebugErrBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        ensureDebugErrIsMutable();
        debugErr_.add(value);
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:com.linbit.linstor.proto.javainternal.MsgDebugReply)
    }

    // @@protoc_insertion_point(class_scope:com.linbit.linstor.proto.javainternal.MsgDebugReply)
    private static final com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply();
    }

    public static com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<MsgDebugReply>
        PARSER = new com.google.protobuf.AbstractParser<MsgDebugReply>() {
      public MsgDebugReply parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new MsgDebugReply(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<MsgDebugReply> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<MsgDebugReply> getParserForType() {
      return PARSER;
    }

    public com.linbit.linstor.proto.javainternal.MsgDebugReplyOuterClass.MsgDebugReply getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_linbit_linstor_proto_javainternal_MsgDebugReply_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_linbit_linstor_proto_javainternal_MsgDebugReply_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n.linstor/proto/javainternal/MsgDebugRep" +
      "ly.proto\022%com.linbit.linstor.proto.javai" +
      "nternal\"5\n\rMsgDebugReply\022\021\n\tdebug_out\030\001 " +
      "\003(\t\022\021\n\tdebug_err\030\002 \003(\tb\006proto3"
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
        }, assigner);
    internal_static_com_linbit_linstor_proto_javainternal_MsgDebugReply_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_linbit_linstor_proto_javainternal_MsgDebugReply_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_linbit_linstor_proto_javainternal_MsgDebugReply_descriptor,
        new java.lang.String[] { "DebugOut", "DebugErr", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
