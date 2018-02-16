// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: linstor/proto/javainternal/MsgIntNodeDeletedData.proto

package com.linbit.linstor.proto.javainternal;

public final class MsgIntNodeDeletedDataOuterClass {
  private MsgIntNodeDeletedDataOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface MsgIntNodeDeletedDataOrBuilder extends
      // @@protoc_insertion_point(interface_extends:com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedData)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * Node name
     * </pre>
     *
     * <code>string node_name = 1;</code>
     */
    java.lang.String getNodeName();
    /**
     * <pre>
     * Node name
     * </pre>
     *
     * <code>string node_name = 1;</code>
     */
    com.google.protobuf.ByteString
        getNodeNameBytes();
  }
  /**
   * <pre>
   * linstor - Internal message containing the identifier of a deleted but requested node.
   * This message is basically just to make sure the satellite deletes the node.
   * </pre>
   *
   * Protobuf type {@code com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedData}
   */
  public  static final class MsgIntNodeDeletedData extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedData)
      MsgIntNodeDeletedDataOrBuilder {
    // Use MsgIntNodeDeletedData.newBuilder() to construct.
    private MsgIntNodeDeletedData(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private MsgIntNodeDeletedData() {
      nodeName_ = "";
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private MsgIntNodeDeletedData(
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

              nodeName_ = s;
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
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.internal_static_com_linbit_linstor_proto_javainternal_MsgIntNodeDeletedData_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.internal_static_com_linbit_linstor_proto_javainternal_MsgIntNodeDeletedData_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData.class, com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData.Builder.class);
    }

    public static final int NODE_NAME_FIELD_NUMBER = 1;
    private volatile java.lang.Object nodeName_;
    /**
     * <pre>
     * Node name
     * </pre>
     *
     * <code>string node_name = 1;</code>
     */
    public java.lang.String getNodeName() {
      java.lang.Object ref = nodeName_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        nodeName_ = s;
        return s;
      }
    }
    /**
     * <pre>
     * Node name
     * </pre>
     *
     * <code>string node_name = 1;</code>
     */
    public com.google.protobuf.ByteString
        getNodeNameBytes() {
      java.lang.Object ref = nodeName_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        nodeName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
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
      if (!getNodeNameBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, nodeName_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!getNodeNameBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, nodeName_);
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
      if (!(obj instanceof com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData)) {
        return super.equals(obj);
      }
      com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData other = (com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData) obj;

      boolean result = true;
      result = result && getNodeName()
          .equals(other.getNodeName());
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + NODE_NAME_FIELD_NUMBER;
      hash = (53 * hash) + getNodeName().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData parseFrom(
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
    public static Builder newBuilder(com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData prototype) {
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
     * linstor - Internal message containing the identifier of a deleted but requested node.
     * This message is basically just to make sure the satellite deletes the node.
     * </pre>
     *
     * Protobuf type {@code com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedData}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedData)
        com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedDataOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.internal_static_com_linbit_linstor_proto_javainternal_MsgIntNodeDeletedData_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.internal_static_com_linbit_linstor_proto_javainternal_MsgIntNodeDeletedData_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData.class, com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData.Builder.class);
      }

      // Construct using com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData.newBuilder()
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
        nodeName_ = "";

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.internal_static_com_linbit_linstor_proto_javainternal_MsgIntNodeDeletedData_descriptor;
      }

      public com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData getDefaultInstanceForType() {
        return com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData.getDefaultInstance();
      }

      public com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData build() {
        com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData buildPartial() {
        com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData result = new com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData(this);
        result.nodeName_ = nodeName_;
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
        if (other instanceof com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData) {
          return mergeFrom((com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData other) {
        if (other == com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData.getDefaultInstance()) return this;
        if (!other.getNodeName().isEmpty()) {
          nodeName_ = other.nodeName_;
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
        com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private java.lang.Object nodeName_ = "";
      /**
       * <pre>
       * Node name
       * </pre>
       *
       * <code>string node_name = 1;</code>
       */
      public java.lang.String getNodeName() {
        java.lang.Object ref = nodeName_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          nodeName_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <pre>
       * Node name
       * </pre>
       *
       * <code>string node_name = 1;</code>
       */
      public com.google.protobuf.ByteString
          getNodeNameBytes() {
        java.lang.Object ref = nodeName_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          nodeName_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       * Node name
       * </pre>
       *
       * <code>string node_name = 1;</code>
       */
      public Builder setNodeName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        nodeName_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Node name
       * </pre>
       *
       * <code>string node_name = 1;</code>
       */
      public Builder clearNodeName() {
        
        nodeName_ = getDefaultInstance().getNodeName();
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Node name
       * </pre>
       *
       * <code>string node_name = 1;</code>
       */
      public Builder setNodeNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        nodeName_ = value;
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


      // @@protoc_insertion_point(builder_scope:com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedData)
    }

    // @@protoc_insertion_point(class_scope:com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedData)
    private static final com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData();
    }

    public static com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<MsgIntNodeDeletedData>
        PARSER = new com.google.protobuf.AbstractParser<MsgIntNodeDeletedData>() {
      public MsgIntNodeDeletedData parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new MsgIntNodeDeletedData(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<MsgIntNodeDeletedData> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<MsgIntNodeDeletedData> getParserForType() {
      return PARSER;
    }

    public com.linbit.linstor.proto.javainternal.MsgIntNodeDeletedDataOuterClass.MsgIntNodeDeletedData getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_linbit_linstor_proto_javainternal_MsgIntNodeDeletedData_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_linbit_linstor_proto_javainternal_MsgIntNodeDeletedData_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n6linstor/proto/javainternal/MsgIntNodeD" +
      "eletedData.proto\022%com.linbit.linstor.pro" +
      "to.javainternal\"*\n\025MsgIntNodeDeletedData" +
      "\022\021\n\tnode_name\030\001 \001(\tb\006proto3"
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
    internal_static_com_linbit_linstor_proto_javainternal_MsgIntNodeDeletedData_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_linbit_linstor_proto_javainternal_MsgIntNodeDeletedData_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_linbit_linstor_proto_javainternal_MsgIntNodeDeletedData_descriptor,
        new java.lang.String[] { "NodeName", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
