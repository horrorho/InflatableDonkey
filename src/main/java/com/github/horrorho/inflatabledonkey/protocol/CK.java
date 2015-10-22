/* 
 * The MIT License
 *
 * Copyright 2015 Ahseya.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.horrorho.inflatabledonkey.protocol;

public final class CK {
  private CK() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface RequestOrBuilder extends
      // @@protoc_insertion_point(interface_extends:Request)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional .Info info = 1;</code>
     */
    boolean hasInfo();
    /**
     * <code>optional .Info info = 1;</code>
     */
    CK.Info getInfo();
    /**
     * <code>optional .Info info = 1;</code>
     */
    CK.InfoOrBuilder getInfoOrBuilder();

    /**
     * <code>optional .Message message = 2;</code>
     */
    boolean hasMessage();
    /**
     * <code>optional .Message message = 2;</code>
     */
    CK.Message getMessage();
    /**
     * <code>optional .Message message = 2;</code>
     */
    CK.MessageOrBuilder getMessageOrBuilder();

    /**
     * <code>optional .M201Request m201Request = 201;</code>
     */
    boolean hasM201Request();
    /**
     * <code>optional .M201Request m201Request = 201;</code>
     */
    CK.M201Request getM201Request();
    /**
     * <code>optional .M201Request m201Request = 201;</code>
     */
    CK.M201RequestOrBuilder getM201RequestOrBuilder();

    /**
     * <code>optional .M211Request m211Request = 211;</code>
     */
    boolean hasM211Request();
    /**
     * <code>optional .M211Request m211Request = 211;</code>
     */
    CK.M211Request getM211Request();
    /**
     * <code>optional .M211Request m211Request = 211;</code>
     */
    CK.M211RequestOrBuilder getM211RequestOrBuilder();
  }
  /**
   * Protobuf type {@code Request}
   */
  public static final class Request extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:Request)
      RequestOrBuilder {
    // Use Request.newBuilder() to construct.
    private Request(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Request(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Request defaultInstance;
    public static Request getDefaultInstance() {
      return defaultInstance;
    }

    public Request getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Request(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
              CK.Info.Builder subBuilder = null;
              if (((bitField0_ & 0x00000001) == 0x00000001)) {
                subBuilder = info_.toBuilder();
              }
              info_ = input.readMessage(CK.Info.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(info_);
                info_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000001;
              break;
            }
            case 18: {
              CK.Message.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = message_.toBuilder();
              }
              message_ = input.readMessage(CK.Message.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(message_);
                message_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
            case 1610: {
              CK.M201Request.Builder subBuilder = null;
              if (((bitField0_ & 0x00000004) == 0x00000004)) {
                subBuilder = m201Request_.toBuilder();
              }
              m201Request_ = input.readMessage(CK.M201Request.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(m201Request_);
                m201Request_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000004;
              break;
            }
            case 1690: {
              CK.M211Request.Builder subBuilder = null;
              if (((bitField0_ & 0x00000008) == 0x00000008)) {
                subBuilder = m211Request_.toBuilder();
              }
              m211Request_ = input.readMessage(CK.M211Request.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(m211Request_);
                m211Request_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000008;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_Request_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_Request_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.Request.class, CK.Request.Builder.class);
    }

    public static com.google.protobuf.Parser<Request> PARSER =
        new com.google.protobuf.AbstractParser<Request>() {
      public Request parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Request(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Request> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int INFO_FIELD_NUMBER = 1;
    private CK.Info info_;
    /**
     * <code>optional .Info info = 1;</code>
     */
    public boolean hasInfo() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional .Info info = 1;</code>
     */
    public CK.Info getInfo() {
      return info_;
    }
    /**
     * <code>optional .Info info = 1;</code>
     */
    public CK.InfoOrBuilder getInfoOrBuilder() {
      return info_;
    }

    public static final int MESSAGE_FIELD_NUMBER = 2;
    private CK.Message message_;
    /**
     * <code>optional .Message message = 2;</code>
     */
    public boolean hasMessage() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional .Message message = 2;</code>
     */
    public CK.Message getMessage() {
      return message_;
    }
    /**
     * <code>optional .Message message = 2;</code>
     */
    public CK.MessageOrBuilder getMessageOrBuilder() {
      return message_;
    }

    public static final int M201REQUEST_FIELD_NUMBER = 201;
    private CK.M201Request m201Request_;
    /**
     * <code>optional .M201Request m201Request = 201;</code>
     */
    public boolean hasM201Request() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional .M201Request m201Request = 201;</code>
     */
    public CK.M201Request getM201Request() {
      return m201Request_;
    }
    /**
     * <code>optional .M201Request m201Request = 201;</code>
     */
    public CK.M201RequestOrBuilder getM201RequestOrBuilder() {
      return m201Request_;
    }

    public static final int M211REQUEST_FIELD_NUMBER = 211;
    private CK.M211Request m211Request_;
    /**
     * <code>optional .M211Request m211Request = 211;</code>
     */
    public boolean hasM211Request() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional .M211Request m211Request = 211;</code>
     */
    public CK.M211Request getM211Request() {
      return m211Request_;
    }
    /**
     * <code>optional .M211Request m211Request = 211;</code>
     */
    public CK.M211RequestOrBuilder getM211RequestOrBuilder() {
      return m211Request_;
    }

    private void initFields() {
      info_ = CK.Info.getDefaultInstance();
      message_ = CK.Message.getDefaultInstance();
      m201Request_ = CK.M201Request.getDefaultInstance();
      m211Request_ = CK.M211Request.getDefaultInstance();
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeMessage(1, info_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(2, message_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeMessage(201, m201Request_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeMessage(211, m211Request_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, info_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, message_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(201, m201Request_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(211, m211Request_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.Request parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Request parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Request parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Request parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Request parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Request parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.Request parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.Request parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.Request parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Request parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.Request prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code Request}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:Request)
        CK.RequestOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_Request_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_Request_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.Request.class, CK.Request.Builder.class);
      }

      // Construct using CloudKit.Request.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getInfoFieldBuilder();
          getMessageFieldBuilder();
          getM201RequestFieldBuilder();
          getM211RequestFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (infoBuilder_ == null) {
          info_ = CK.Info.getDefaultInstance();
        } else {
          infoBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        if (messageBuilder_ == null) {
          message_ = CK.Message.getDefaultInstance();
        } else {
          messageBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        if (m201RequestBuilder_ == null) {
          m201Request_ = CK.M201Request.getDefaultInstance();
        } else {
          m201RequestBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        if (m211RequestBuilder_ == null) {
          m211Request_ = CK.M211Request.getDefaultInstance();
        } else {
          m211RequestBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_Request_descriptor;
      }

      public CK.Request getDefaultInstanceForType() {
        return CK.Request.getDefaultInstance();
      }

      public CK.Request build() {
        CK.Request result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.Request buildPartial() {
        CK.Request result = new CK.Request(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (infoBuilder_ == null) {
          result.info_ = info_;
        } else {
          result.info_ = infoBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (messageBuilder_ == null) {
          result.message_ = message_;
        } else {
          result.message_ = messageBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        if (m201RequestBuilder_ == null) {
          result.m201Request_ = m201Request_;
        } else {
          result.m201Request_ = m201RequestBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        if (m211RequestBuilder_ == null) {
          result.m211Request_ = m211Request_;
        } else {
          result.m211Request_ = m211RequestBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.Request) {
          return mergeFrom((CK.Request)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.Request other) {
        if (other == CK.Request.getDefaultInstance()) return this;
        if (other.hasInfo()) {
          mergeInfo(other.getInfo());
        }
        if (other.hasMessage()) {
          mergeMessage(other.getMessage());
        }
        if (other.hasM201Request()) {
          mergeM201Request(other.getM201Request());
        }
        if (other.hasM211Request()) {
          mergeM211Request(other.getM211Request());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.Request parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.Request) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private CK.Info info_ = CK.Info.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Info, CK.Info.Builder, CK.InfoOrBuilder> infoBuilder_;
      /**
       * <code>optional .Info info = 1;</code>
       */
      public boolean hasInfo() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional .Info info = 1;</code>
       */
      public CK.Info getInfo() {
        if (infoBuilder_ == null) {
          return info_;
        } else {
          return infoBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .Info info = 1;</code>
       */
      public Builder setInfo(CK.Info value) {
        if (infoBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          info_ = value;
          onChanged();
        } else {
          infoBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .Info info = 1;</code>
       */
      public Builder setInfo(
          CK.Info.Builder builderForValue) {
        if (infoBuilder_ == null) {
          info_ = builderForValue.build();
          onChanged();
        } else {
          infoBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .Info info = 1;</code>
       */
      public Builder mergeInfo(CK.Info value) {
        if (infoBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              info_ != CK.Info.getDefaultInstance()) {
            info_ =
              CK.Info.newBuilder(info_).mergeFrom(value).buildPartial();
          } else {
            info_ = value;
          }
          onChanged();
        } else {
          infoBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .Info info = 1;</code>
       */
      public Builder clearInfo() {
        if (infoBuilder_ == null) {
          info_ = CK.Info.getDefaultInstance();
          onChanged();
        } else {
          infoBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      /**
       * <code>optional .Info info = 1;</code>
       */
      public CK.Info.Builder getInfoBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getInfoFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Info info = 1;</code>
       */
      public CK.InfoOrBuilder getInfoOrBuilder() {
        if (infoBuilder_ != null) {
          return infoBuilder_.getMessageOrBuilder();
        } else {
          return info_;
        }
      }
      /**
       * <code>optional .Info info = 1;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Info, CK.Info.Builder, CK.InfoOrBuilder> 
          getInfoFieldBuilder() {
        if (infoBuilder_ == null) {
          infoBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Info, CK.Info.Builder, CK.InfoOrBuilder>(
                  getInfo(),
                  getParentForChildren(),
                  isClean());
          info_ = null;
        }
        return infoBuilder_;
      }

      private CK.Message message_ = CK.Message.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Message, CK.Message.Builder, CK.MessageOrBuilder> messageBuilder_;
      /**
       * <code>optional .Message message = 2;</code>
       */
      public boolean hasMessage() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional .Message message = 2;</code>
       */
      public CK.Message getMessage() {
        if (messageBuilder_ == null) {
          return message_;
        } else {
          return messageBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .Message message = 2;</code>
       */
      public Builder setMessage(CK.Message value) {
        if (messageBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          message_ = value;
          onChanged();
        } else {
          messageBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Message message = 2;</code>
       */
      public Builder setMessage(
          CK.Message.Builder builderForValue) {
        if (messageBuilder_ == null) {
          message_ = builderForValue.build();
          onChanged();
        } else {
          messageBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Message message = 2;</code>
       */
      public Builder mergeMessage(CK.Message value) {
        if (messageBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              message_ != CK.Message.getDefaultInstance()) {
            message_ =
              CK.Message.newBuilder(message_).mergeFrom(value).buildPartial();
          } else {
            message_ = value;
          }
          onChanged();
        } else {
          messageBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Message message = 2;</code>
       */
      public Builder clearMessage() {
        if (messageBuilder_ == null) {
          message_ = CK.Message.getDefaultInstance();
          onChanged();
        } else {
          messageBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      /**
       * <code>optional .Message message = 2;</code>
       */
      public CK.Message.Builder getMessageBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getMessageFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Message message = 2;</code>
       */
      public CK.MessageOrBuilder getMessageOrBuilder() {
        if (messageBuilder_ != null) {
          return messageBuilder_.getMessageOrBuilder();
        } else {
          return message_;
        }
      }
      /**
       * <code>optional .Message message = 2;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Message, CK.Message.Builder, CK.MessageOrBuilder> 
          getMessageFieldBuilder() {
        if (messageBuilder_ == null) {
          messageBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Message, CK.Message.Builder, CK.MessageOrBuilder>(
                  getMessage(),
                  getParentForChildren(),
                  isClean());
          message_ = null;
        }
        return messageBuilder_;
      }

      private CK.M201Request m201Request_ = CK.M201Request.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.M201Request, CK.M201Request.Builder, CK.M201RequestOrBuilder> m201RequestBuilder_;
      /**
       * <code>optional .M201Request m201Request = 201;</code>
       */
      public boolean hasM201Request() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional .M201Request m201Request = 201;</code>
       */
      public CK.M201Request getM201Request() {
        if (m201RequestBuilder_ == null) {
          return m201Request_;
        } else {
          return m201RequestBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .M201Request m201Request = 201;</code>
       */
      public Builder setM201Request(CK.M201Request value) {
        if (m201RequestBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          m201Request_ = value;
          onChanged();
        } else {
          m201RequestBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      /**
       * <code>optional .M201Request m201Request = 201;</code>
       */
      public Builder setM201Request(
          CK.M201Request.Builder builderForValue) {
        if (m201RequestBuilder_ == null) {
          m201Request_ = builderForValue.build();
          onChanged();
        } else {
          m201RequestBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      /**
       * <code>optional .M201Request m201Request = 201;</code>
       */
      public Builder mergeM201Request(CK.M201Request value) {
        if (m201RequestBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004) &&
              m201Request_ != CK.M201Request.getDefaultInstance()) {
            m201Request_ =
              CK.M201Request.newBuilder(m201Request_).mergeFrom(value).buildPartial();
          } else {
            m201Request_ = value;
          }
          onChanged();
        } else {
          m201RequestBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      /**
       * <code>optional .M201Request m201Request = 201;</code>
       */
      public Builder clearM201Request() {
        if (m201RequestBuilder_ == null) {
          m201Request_ = CK.M201Request.getDefaultInstance();
          onChanged();
        } else {
          m201RequestBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      /**
       * <code>optional .M201Request m201Request = 201;</code>
       */
      public CK.M201Request.Builder getM201RequestBuilder() {
        bitField0_ |= 0x00000004;
        onChanged();
        return getM201RequestFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .M201Request m201Request = 201;</code>
       */
      public CK.M201RequestOrBuilder getM201RequestOrBuilder() {
        if (m201RequestBuilder_ != null) {
          return m201RequestBuilder_.getMessageOrBuilder();
        } else {
          return m201Request_;
        }
      }
      /**
       * <code>optional .M201Request m201Request = 201;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.M201Request, CK.M201Request.Builder, CK.M201RequestOrBuilder> 
          getM201RequestFieldBuilder() {
        if (m201RequestBuilder_ == null) {
          m201RequestBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.M201Request, CK.M201Request.Builder, CK.M201RequestOrBuilder>(
                  getM201Request(),
                  getParentForChildren(),
                  isClean());
          m201Request_ = null;
        }
        return m201RequestBuilder_;
      }

      private CK.M211Request m211Request_ = CK.M211Request.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.M211Request, CK.M211Request.Builder, CK.M211RequestOrBuilder> m211RequestBuilder_;
      /**
       * <code>optional .M211Request m211Request = 211;</code>
       */
      public boolean hasM211Request() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>optional .M211Request m211Request = 211;</code>
       */
      public CK.M211Request getM211Request() {
        if (m211RequestBuilder_ == null) {
          return m211Request_;
        } else {
          return m211RequestBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .M211Request m211Request = 211;</code>
       */
      public Builder setM211Request(CK.M211Request value) {
        if (m211RequestBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          m211Request_ = value;
          onChanged();
        } else {
          m211RequestBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .M211Request m211Request = 211;</code>
       */
      public Builder setM211Request(
          CK.M211Request.Builder builderForValue) {
        if (m211RequestBuilder_ == null) {
          m211Request_ = builderForValue.build();
          onChanged();
        } else {
          m211RequestBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .M211Request m211Request = 211;</code>
       */
      public Builder mergeM211Request(CK.M211Request value) {
        if (m211RequestBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008) &&
              m211Request_ != CK.M211Request.getDefaultInstance()) {
            m211Request_ =
              CK.M211Request.newBuilder(m211Request_).mergeFrom(value).buildPartial();
          } else {
            m211Request_ = value;
          }
          onChanged();
        } else {
          m211RequestBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .M211Request m211Request = 211;</code>
       */
      public Builder clearM211Request() {
        if (m211RequestBuilder_ == null) {
          m211Request_ = CK.M211Request.getDefaultInstance();
          onChanged();
        } else {
          m211RequestBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      /**
       * <code>optional .M211Request m211Request = 211;</code>
       */
      public CK.M211Request.Builder getM211RequestBuilder() {
        bitField0_ |= 0x00000008;
        onChanged();
        return getM211RequestFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .M211Request m211Request = 211;</code>
       */
      public CK.M211RequestOrBuilder getM211RequestOrBuilder() {
        if (m211RequestBuilder_ != null) {
          return m211RequestBuilder_.getMessageOrBuilder();
        } else {
          return m211Request_;
        }
      }
      /**
       * <code>optional .M211Request m211Request = 211;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.M211Request, CK.M211Request.Builder, CK.M211RequestOrBuilder> 
          getM211RequestFieldBuilder() {
        if (m211RequestBuilder_ == null) {
          m211RequestBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.M211Request, CK.M211Request.Builder, CK.M211RequestOrBuilder>(
                  getM211Request(),
                  getParentForChildren(),
                  isClean());
          m211Request_ = null;
        }
        return m211RequestBuilder_;
      }

      // @@protoc_insertion_point(builder_scope:Request)
    }

    static {
      defaultInstance = new Request(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:Request)
  }

  public interface ResponseOrBuilder extends
      // @@protoc_insertion_point(interface_extends:Response)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional uint32 f1 = 1;</code>
     */
    boolean hasF1();
    /**
     * <code>optional uint32 f1 = 1;</code>
     */
    int getF1();

    /**
     * <code>optional .Message message = 2;</code>
     */
    boolean hasMessage();
    /**
     * <code>optional .Message message = 2;</code>
     */
    CK.Message getMessage();
    /**
     * <code>optional .Message message = 2;</code>
     */
    CK.MessageOrBuilder getMessageOrBuilder();

    /**
     * <code>optional .Status status = 3;</code>
     */
    boolean hasStatus();
    /**
     * <code>optional .Status status = 3;</code>
     */
    CK.Status getStatus();
    /**
     * <code>optional .Status status = 3;</code>
     */
    CK.StatusOrBuilder getStatusOrBuilder();

    /**
     * <code>optional .M201Response m201Response = 201;</code>
     */
    boolean hasM201Response();
    /**
     * <code>optional .M201Response m201Response = 201;</code>
     */
    CK.M201Response getM201Response();
    /**
     * <code>optional .M201Response m201Response = 201;</code>
     */
    CK.M201ResponseOrBuilder getM201ResponseOrBuilder();

    /**
     * <code>optional .M211Response m211Response = 211;</code>
     */
    boolean hasM211Response();
    /**
     * <code>optional .M211Response m211Response = 211;</code>
     */
    CK.M211Response getM211Response();
    /**
     * <code>optional .M211Response m211Response = 211;</code>
     */
    CK.M211ResponseOrBuilder getM211ResponseOrBuilder();
  }
  /**
   * Protobuf type {@code Response}
   */
  public static final class Response extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:Response)
      ResponseOrBuilder {
    // Use Response.newBuilder() to construct.
    private Response(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Response(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Response defaultInstance;
    public static Response getDefaultInstance() {
      return defaultInstance;
    }

    public Response getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Response(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
            case 8: {
              bitField0_ |= 0x00000001;
              f1_ = input.readUInt32();
              break;
            }
            case 18: {
              CK.Message.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = message_.toBuilder();
              }
              message_ = input.readMessage(CK.Message.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(message_);
                message_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
            case 26: {
              CK.Status.Builder subBuilder = null;
              if (((bitField0_ & 0x00000004) == 0x00000004)) {
                subBuilder = status_.toBuilder();
              }
              status_ = input.readMessage(CK.Status.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(status_);
                status_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000004;
              break;
            }
            case 1610: {
              CK.M201Response.Builder subBuilder = null;
              if (((bitField0_ & 0x00000008) == 0x00000008)) {
                subBuilder = m201Response_.toBuilder();
              }
              m201Response_ = input.readMessage(CK.M201Response.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(m201Response_);
                m201Response_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000008;
              break;
            }
            case 1690: {
              CK.M211Response.Builder subBuilder = null;
              if (((bitField0_ & 0x00000010) == 0x00000010)) {
                subBuilder = m211Response_.toBuilder();
              }
              m211Response_ = input.readMessage(CK.M211Response.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(m211Response_);
                m211Response_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000010;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_Response_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_Response_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.Response.class, CK.Response.Builder.class);
    }

    public static com.google.protobuf.Parser<Response> PARSER =
        new com.google.protobuf.AbstractParser<Response>() {
      public Response parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Response(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Response> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int F1_FIELD_NUMBER = 1;
    private int f1_;
    /**
     * <code>optional uint32 f1 = 1;</code>
     */
    public boolean hasF1() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional uint32 f1 = 1;</code>
     */
    public int getF1() {
      return f1_;
    }

    public static final int MESSAGE_FIELD_NUMBER = 2;
    private CK.Message message_;
    /**
     * <code>optional .Message message = 2;</code>
     */
    public boolean hasMessage() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional .Message message = 2;</code>
     */
    public CK.Message getMessage() {
      return message_;
    }
    /**
     * <code>optional .Message message = 2;</code>
     */
    public CK.MessageOrBuilder getMessageOrBuilder() {
      return message_;
    }

    public static final int STATUS_FIELD_NUMBER = 3;
    private CK.Status status_;
    /**
     * <code>optional .Status status = 3;</code>
     */
    public boolean hasStatus() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional .Status status = 3;</code>
     */
    public CK.Status getStatus() {
      return status_;
    }
    /**
     * <code>optional .Status status = 3;</code>
     */
    public CK.StatusOrBuilder getStatusOrBuilder() {
      return status_;
    }

    public static final int M201RESPONSE_FIELD_NUMBER = 201;
    private CK.M201Response m201Response_;
    /**
     * <code>optional .M201Response m201Response = 201;</code>
     */
    public boolean hasM201Response() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional .M201Response m201Response = 201;</code>
     */
    public CK.M201Response getM201Response() {
      return m201Response_;
    }
    /**
     * <code>optional .M201Response m201Response = 201;</code>
     */
    public CK.M201ResponseOrBuilder getM201ResponseOrBuilder() {
      return m201Response_;
    }

    public static final int M211RESPONSE_FIELD_NUMBER = 211;
    private CK.M211Response m211Response_;
    /**
     * <code>optional .M211Response m211Response = 211;</code>
     */
    public boolean hasM211Response() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    /**
     * <code>optional .M211Response m211Response = 211;</code>
     */
    public CK.M211Response getM211Response() {
      return m211Response_;
    }
    /**
     * <code>optional .M211Response m211Response = 211;</code>
     */
    public CK.M211ResponseOrBuilder getM211ResponseOrBuilder() {
      return m211Response_;
    }

    private void initFields() {
      f1_ = 0;
      message_ = CK.Message.getDefaultInstance();
      status_ = CK.Status.getDefaultInstance();
      m201Response_ = CK.M201Response.getDefaultInstance();
      m211Response_ = CK.M211Response.getDefaultInstance();
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeUInt32(1, f1_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(2, message_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeMessage(3, status_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeMessage(201, m201Response_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeMessage(211, m211Response_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(1, f1_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, message_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, status_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(201, m201Response_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(211, m211Response_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.Response parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Response parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Response parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Response parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Response parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Response parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.Response parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.Response parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.Response parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Response parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.Response prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code Response}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:Response)
        CK.ResponseOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_Response_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_Response_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.Response.class, CK.Response.Builder.class);
      }

      // Construct using CloudKit.Response.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getMessageFieldBuilder();
          getStatusFieldBuilder();
          getM201ResponseFieldBuilder();
          getM211ResponseFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        f1_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        if (messageBuilder_ == null) {
          message_ = CK.Message.getDefaultInstance();
        } else {
          messageBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        if (statusBuilder_ == null) {
          status_ = CK.Status.getDefaultInstance();
        } else {
          statusBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        if (m201ResponseBuilder_ == null) {
          m201Response_ = CK.M201Response.getDefaultInstance();
        } else {
          m201ResponseBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        if (m211ResponseBuilder_ == null) {
          m211Response_ = CK.M211Response.getDefaultInstance();
        } else {
          m211ResponseBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_Response_descriptor;
      }

      public CK.Response getDefaultInstanceForType() {
        return CK.Response.getDefaultInstance();
      }

      public CK.Response build() {
        CK.Response result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.Response buildPartial() {
        CK.Response result = new CK.Response(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.f1_ = f1_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (messageBuilder_ == null) {
          result.message_ = message_;
        } else {
          result.message_ = messageBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        if (statusBuilder_ == null) {
          result.status_ = status_;
        } else {
          result.status_ = statusBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        if (m201ResponseBuilder_ == null) {
          result.m201Response_ = m201Response_;
        } else {
          result.m201Response_ = m201ResponseBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        if (m211ResponseBuilder_ == null) {
          result.m211Response_ = m211Response_;
        } else {
          result.m211Response_ = m211ResponseBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.Response) {
          return mergeFrom((CK.Response)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.Response other) {
        if (other == CK.Response.getDefaultInstance()) return this;
        if (other.hasF1()) {
          setF1(other.getF1());
        }
        if (other.hasMessage()) {
          mergeMessage(other.getMessage());
        }
        if (other.hasStatus()) {
          mergeStatus(other.getStatus());
        }
        if (other.hasM201Response()) {
          mergeM201Response(other.getM201Response());
        }
        if (other.hasM211Response()) {
          mergeM211Response(other.getM211Response());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.Response parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.Response) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int f1_ ;
      /**
       * <code>optional uint32 f1 = 1;</code>
       */
      public boolean hasF1() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional uint32 f1 = 1;</code>
       */
      public int getF1() {
        return f1_;
      }
      /**
       * <code>optional uint32 f1 = 1;</code>
       */
      public Builder setF1(int value) {
        bitField0_ |= 0x00000001;
        f1_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 f1 = 1;</code>
       */
      public Builder clearF1() {
        bitField0_ = (bitField0_ & ~0x00000001);
        f1_ = 0;
        onChanged();
        return this;
      }

      private CK.Message message_ = CK.Message.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Message, CK.Message.Builder, CK.MessageOrBuilder> messageBuilder_;
      /**
       * <code>optional .Message message = 2;</code>
       */
      public boolean hasMessage() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional .Message message = 2;</code>
       */
      public CK.Message getMessage() {
        if (messageBuilder_ == null) {
          return message_;
        } else {
          return messageBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .Message message = 2;</code>
       */
      public Builder setMessage(CK.Message value) {
        if (messageBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          message_ = value;
          onChanged();
        } else {
          messageBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Message message = 2;</code>
       */
      public Builder setMessage(
          CK.Message.Builder builderForValue) {
        if (messageBuilder_ == null) {
          message_ = builderForValue.build();
          onChanged();
        } else {
          messageBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Message message = 2;</code>
       */
      public Builder mergeMessage(CK.Message value) {
        if (messageBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              message_ != CK.Message.getDefaultInstance()) {
            message_ =
              CK.Message.newBuilder(message_).mergeFrom(value).buildPartial();
          } else {
            message_ = value;
          }
          onChanged();
        } else {
          messageBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Message message = 2;</code>
       */
      public Builder clearMessage() {
        if (messageBuilder_ == null) {
          message_ = CK.Message.getDefaultInstance();
          onChanged();
        } else {
          messageBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      /**
       * <code>optional .Message message = 2;</code>
       */
      public CK.Message.Builder getMessageBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getMessageFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Message message = 2;</code>
       */
      public CK.MessageOrBuilder getMessageOrBuilder() {
        if (messageBuilder_ != null) {
          return messageBuilder_.getMessageOrBuilder();
        } else {
          return message_;
        }
      }
      /**
       * <code>optional .Message message = 2;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Message, CK.Message.Builder, CK.MessageOrBuilder> 
          getMessageFieldBuilder() {
        if (messageBuilder_ == null) {
          messageBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Message, CK.Message.Builder, CK.MessageOrBuilder>(
                  getMessage(),
                  getParentForChildren(),
                  isClean());
          message_ = null;
        }
        return messageBuilder_;
      }

      private CK.Status status_ = CK.Status.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Status, CK.Status.Builder, CK.StatusOrBuilder> statusBuilder_;
      /**
       * <code>optional .Status status = 3;</code>
       */
      public boolean hasStatus() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional .Status status = 3;</code>
       */
      public CK.Status getStatus() {
        if (statusBuilder_ == null) {
          return status_;
        } else {
          return statusBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .Status status = 3;</code>
       */
      public Builder setStatus(CK.Status value) {
        if (statusBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          status_ = value;
          onChanged();
        } else {
          statusBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      /**
       * <code>optional .Status status = 3;</code>
       */
      public Builder setStatus(
          CK.Status.Builder builderForValue) {
        if (statusBuilder_ == null) {
          status_ = builderForValue.build();
          onChanged();
        } else {
          statusBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      /**
       * <code>optional .Status status = 3;</code>
       */
      public Builder mergeStatus(CK.Status value) {
        if (statusBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004) &&
              status_ != CK.Status.getDefaultInstance()) {
            status_ =
              CK.Status.newBuilder(status_).mergeFrom(value).buildPartial();
          } else {
            status_ = value;
          }
          onChanged();
        } else {
          statusBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      /**
       * <code>optional .Status status = 3;</code>
       */
      public Builder clearStatus() {
        if (statusBuilder_ == null) {
          status_ = CK.Status.getDefaultInstance();
          onChanged();
        } else {
          statusBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      /**
       * <code>optional .Status status = 3;</code>
       */
      public CK.Status.Builder getStatusBuilder() {
        bitField0_ |= 0x00000004;
        onChanged();
        return getStatusFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Status status = 3;</code>
       */
      public CK.StatusOrBuilder getStatusOrBuilder() {
        if (statusBuilder_ != null) {
          return statusBuilder_.getMessageOrBuilder();
        } else {
          return status_;
        }
      }
      /**
       * <code>optional .Status status = 3;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Status, CK.Status.Builder, CK.StatusOrBuilder> 
          getStatusFieldBuilder() {
        if (statusBuilder_ == null) {
          statusBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Status, CK.Status.Builder, CK.StatusOrBuilder>(
                  getStatus(),
                  getParentForChildren(),
                  isClean());
          status_ = null;
        }
        return statusBuilder_;
      }

      private CK.M201Response m201Response_ = CK.M201Response.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.M201Response, CK.M201Response.Builder, CK.M201ResponseOrBuilder> m201ResponseBuilder_;
      /**
       * <code>optional .M201Response m201Response = 201;</code>
       */
      public boolean hasM201Response() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>optional .M201Response m201Response = 201;</code>
       */
      public CK.M201Response getM201Response() {
        if (m201ResponseBuilder_ == null) {
          return m201Response_;
        } else {
          return m201ResponseBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .M201Response m201Response = 201;</code>
       */
      public Builder setM201Response(CK.M201Response value) {
        if (m201ResponseBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          m201Response_ = value;
          onChanged();
        } else {
          m201ResponseBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .M201Response m201Response = 201;</code>
       */
      public Builder setM201Response(
          CK.M201Response.Builder builderForValue) {
        if (m201ResponseBuilder_ == null) {
          m201Response_ = builderForValue.build();
          onChanged();
        } else {
          m201ResponseBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .M201Response m201Response = 201;</code>
       */
      public Builder mergeM201Response(CK.M201Response value) {
        if (m201ResponseBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008) &&
              m201Response_ != CK.M201Response.getDefaultInstance()) {
            m201Response_ =
              CK.M201Response.newBuilder(m201Response_).mergeFrom(value).buildPartial();
          } else {
            m201Response_ = value;
          }
          onChanged();
        } else {
          m201ResponseBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .M201Response m201Response = 201;</code>
       */
      public Builder clearM201Response() {
        if (m201ResponseBuilder_ == null) {
          m201Response_ = CK.M201Response.getDefaultInstance();
          onChanged();
        } else {
          m201ResponseBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      /**
       * <code>optional .M201Response m201Response = 201;</code>
       */
      public CK.M201Response.Builder getM201ResponseBuilder() {
        bitField0_ |= 0x00000008;
        onChanged();
        return getM201ResponseFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .M201Response m201Response = 201;</code>
       */
      public CK.M201ResponseOrBuilder getM201ResponseOrBuilder() {
        if (m201ResponseBuilder_ != null) {
          return m201ResponseBuilder_.getMessageOrBuilder();
        } else {
          return m201Response_;
        }
      }
      /**
       * <code>optional .M201Response m201Response = 201;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.M201Response, CK.M201Response.Builder, CK.M201ResponseOrBuilder> 
          getM201ResponseFieldBuilder() {
        if (m201ResponseBuilder_ == null) {
          m201ResponseBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.M201Response, CK.M201Response.Builder, CK.M201ResponseOrBuilder>(
                  getM201Response(),
                  getParentForChildren(),
                  isClean());
          m201Response_ = null;
        }
        return m201ResponseBuilder_;
      }

      private CK.M211Response m211Response_ = CK.M211Response.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.M211Response, CK.M211Response.Builder, CK.M211ResponseOrBuilder> m211ResponseBuilder_;
      /**
       * <code>optional .M211Response m211Response = 211;</code>
       */
      public boolean hasM211Response() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      /**
       * <code>optional .M211Response m211Response = 211;</code>
       */
      public CK.M211Response getM211Response() {
        if (m211ResponseBuilder_ == null) {
          return m211Response_;
        } else {
          return m211ResponseBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .M211Response m211Response = 211;</code>
       */
      public Builder setM211Response(CK.M211Response value) {
        if (m211ResponseBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          m211Response_ = value;
          onChanged();
        } else {
          m211ResponseBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      /**
       * <code>optional .M211Response m211Response = 211;</code>
       */
      public Builder setM211Response(
          CK.M211Response.Builder builderForValue) {
        if (m211ResponseBuilder_ == null) {
          m211Response_ = builderForValue.build();
          onChanged();
        } else {
          m211ResponseBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      /**
       * <code>optional .M211Response m211Response = 211;</code>
       */
      public Builder mergeM211Response(CK.M211Response value) {
        if (m211ResponseBuilder_ == null) {
          if (((bitField0_ & 0x00000010) == 0x00000010) &&
              m211Response_ != CK.M211Response.getDefaultInstance()) {
            m211Response_ =
              CK.M211Response.newBuilder(m211Response_).mergeFrom(value).buildPartial();
          } else {
            m211Response_ = value;
          }
          onChanged();
        } else {
          m211ResponseBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      /**
       * <code>optional .M211Response m211Response = 211;</code>
       */
      public Builder clearM211Response() {
        if (m211ResponseBuilder_ == null) {
          m211Response_ = CK.M211Response.getDefaultInstance();
          onChanged();
        } else {
          m211ResponseBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }
      /**
       * <code>optional .M211Response m211Response = 211;</code>
       */
      public CK.M211Response.Builder getM211ResponseBuilder() {
        bitField0_ |= 0x00000010;
        onChanged();
        return getM211ResponseFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .M211Response m211Response = 211;</code>
       */
      public CK.M211ResponseOrBuilder getM211ResponseOrBuilder() {
        if (m211ResponseBuilder_ != null) {
          return m211ResponseBuilder_.getMessageOrBuilder();
        } else {
          return m211Response_;
        }
      }
      /**
       * <code>optional .M211Response m211Response = 211;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.M211Response, CK.M211Response.Builder, CK.M211ResponseOrBuilder> 
          getM211ResponseFieldBuilder() {
        if (m211ResponseBuilder_ == null) {
          m211ResponseBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.M211Response, CK.M211Response.Builder, CK.M211ResponseOrBuilder>(
                  getM211Response(),
                  getParentForChildren(),
                  isClean());
          m211Response_ = null;
        }
        return m211ResponseBuilder_;
      }

      // @@protoc_insertion_point(builder_scope:Response)
    }

    static {
      defaultInstance = new Response(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:Response)
  }

  public interface InfoOrBuilder extends
      // @@protoc_insertion_point(interface_extends:Info)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string container = 2;</code>
     */
    boolean hasContainer();
    /**
     * <code>optional string container = 2;</code>
     */
    java.lang.String getContainer();
    /**
     * <code>optional string container = 2;</code>
     */
    com.google.protobuf.ByteString
        getContainerBytes();

    /**
     * <code>optional string bundle = 3;</code>
     */
    boolean hasBundle();
    /**
     * <code>optional string bundle = 3;</code>
     */
    java.lang.String getBundle();
    /**
     * <code>optional string bundle = 3;</code>
     */
    com.google.protobuf.ByteString
        getBundleBytes();

    /**
     * <code>optional .Item f7 = 7;</code>
     */
    boolean hasF7();
    /**
     * <code>optional .Item f7 = 7;</code>
     */
    CK.Item getF7();
    /**
     * <code>optional .Item f7 = 7;</code>
     */
    CK.ItemOrBuilder getF7OrBuilder();

    /**
     * <code>optional string os = 8;</code>
     */
    boolean hasOs();
    /**
     * <code>optional string os = 8;</code>
     */
    java.lang.String getOs();
    /**
     * <code>optional string os = 8;</code>
     */
    com.google.protobuf.ByteString
        getOsBytes();

    /**
     * <code>optional .Fixed64 f9 = 9;</code>
     */
    boolean hasF9();
    /**
     * <code>optional .Fixed64 f9 = 9;</code>
     */
    CK.Fixed64 getF9();
    /**
     * <code>optional .Fixed64 f9 = 9;</code>
     */
    CK.Fixed64OrBuilder getF9OrBuilder();

    /**
     * <code>optional string app = 10;</code>
     */
    boolean hasApp();
    /**
     * <code>optional string app = 10;</code>
     */
    java.lang.String getApp();
    /**
     * <code>optional string app = 10;</code>
     */
    com.google.protobuf.ByteString
        getAppBytes();

    /**
     * <code>optional string appVersion = 11;</code>
     */
    boolean hasAppVersion();
    /**
     * <code>optional string appVersion = 11;</code>
     */
    java.lang.String getAppVersion();
    /**
     * <code>optional string appVersion = 11;</code>
     */
    com.google.protobuf.ByteString
        getAppVersionBytes();

    /**
     * <code>optional string operation = 12;</code>
     */
    boolean hasOperation();
    /**
     * <code>optional string operation = 12;</code>
     */
    java.lang.String getOperation();
    /**
     * <code>optional string operation = 12;</code>
     */
    com.google.protobuf.ByteString
        getOperationBytes();

    /**
     * <code>optional uint32 limit1 = 13;</code>
     */
    boolean hasLimit1();
    /**
     * <code>optional uint32 limit1 = 13;</code>
     */
    int getLimit1();

    /**
     * <code>optional uint32 limit2 = 14;</code>
     */
    boolean hasLimit2();
    /**
     * <code>optional uint32 limit2 = 14;</code>
     */
    int getLimit2();

    /**
     * <code>optional fixed32 hex32 = 15;</code>
     */
    boolean hasHex32();
    /**
     * <code>optional fixed32 hex32 = 15;</code>
     */
    int getHex32();

    /**
     * <code>optional string version = 18;</code>
     */
    boolean hasVersion();
    /**
     * <code>optional string version = 18;</code>
     */
    java.lang.String getVersion();
    /**
     * <code>optional string version = 18;</code>
     */
    com.google.protobuf.ByteString
        getVersionBytes();

    /**
     * <code>optional uint32 f19 = 19;</code>
     */
    boolean hasF19();
    /**
     * <code>optional uint32 f19 = 19;</code>
     */
    int getF19();

    /**
     * <code>optional string deviceName = 21;</code>
     */
    boolean hasDeviceName();
    /**
     * <code>optional string deviceName = 21;</code>
     */
    java.lang.String getDeviceName();
    /**
     * <code>optional string deviceName = 21;</code>
     */
    com.google.protobuf.ByteString
        getDeviceNameBytes();

    /**
     * <code>optional string deviceID = 22;</code>
     */
    boolean hasDeviceID();
    /**
     * <code>optional string deviceID = 22;</code>
     */
    java.lang.String getDeviceID();
    /**
     * <code>optional string deviceID = 22;</code>
     */
    com.google.protobuf.ByteString
        getDeviceIDBytes();

    /**
     * <code>optional uint32 f23 = 23;</code>
     */
    boolean hasF23();
    /**
     * <code>optional uint32 f23 = 23;</code>
     */
    int getF23();

    /**
     * <code>optional uint32 f25 = 25;</code>
     */
    boolean hasF25();
    /**
     * <code>optional uint32 f25 = 25;</code>
     */
    int getF25();
  }
  /**
   * Protobuf type {@code Info}
   */
  public static final class Info extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:Info)
      InfoOrBuilder {
    // Use Info.newBuilder() to construct.
    private Info(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Info(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Info defaultInstance;
    public static Info getDefaultInstance() {
      return defaultInstance;
    }

    public Info getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Info(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
            case 18: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000001;
              container_ = bs;
              break;
            }
            case 26: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000002;
              bundle_ = bs;
              break;
            }
            case 58: {
              CK.Item.Builder subBuilder = null;
              if (((bitField0_ & 0x00000004) == 0x00000004)) {
                subBuilder = f7_.toBuilder();
              }
              f7_ = input.readMessage(CK.Item.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(f7_);
                f7_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000004;
              break;
            }
            case 66: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000008;
              os_ = bs;
              break;
            }
            case 74: {
              CK.Fixed64.Builder subBuilder = null;
              if (((bitField0_ & 0x00000010) == 0x00000010)) {
                subBuilder = f9_.toBuilder();
              }
              f9_ = input.readMessage(CK.Fixed64.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(f9_);
                f9_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000010;
              break;
            }
            case 82: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000020;
              app_ = bs;
              break;
            }
            case 90: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000040;
              appVersion_ = bs;
              break;
            }
            case 98: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000080;
              operation_ = bs;
              break;
            }
            case 104: {
              bitField0_ |= 0x00000100;
              limit1_ = input.readUInt32();
              break;
            }
            case 112: {
              bitField0_ |= 0x00000200;
              limit2_ = input.readUInt32();
              break;
            }
            case 125: {
              bitField0_ |= 0x00000400;
              hex32_ = input.readFixed32();
              break;
            }
            case 146: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000800;
              version_ = bs;
              break;
            }
            case 152: {
              bitField0_ |= 0x00001000;
              f19_ = input.readUInt32();
              break;
            }
            case 170: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00002000;
              deviceName_ = bs;
              break;
            }
            case 178: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00004000;
              deviceID_ = bs;
              break;
            }
            case 184: {
              bitField0_ |= 0x00008000;
              f23_ = input.readUInt32();
              break;
            }
            case 200: {
              bitField0_ |= 0x00010000;
              f25_ = input.readUInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_Info_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_Info_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.Info.class, CK.Info.Builder.class);
    }

    public static com.google.protobuf.Parser<Info> PARSER =
        new com.google.protobuf.AbstractParser<Info>() {
      public Info parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Info(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Info> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int CONTAINER_FIELD_NUMBER = 2;
    private java.lang.Object container_;
    /**
     * <code>optional string container = 2;</code>
     */
    public boolean hasContainer() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional string container = 2;</code>
     */
    public java.lang.String getContainer() {
      java.lang.Object ref = container_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          container_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string container = 2;</code>
     */
    public com.google.protobuf.ByteString
        getContainerBytes() {
      java.lang.Object ref = container_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        container_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int BUNDLE_FIELD_NUMBER = 3;
    private java.lang.Object bundle_;
    /**
     * <code>optional string bundle = 3;</code>
     */
    public boolean hasBundle() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional string bundle = 3;</code>
     */
    public java.lang.String getBundle() {
      java.lang.Object ref = bundle_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          bundle_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string bundle = 3;</code>
     */
    public com.google.protobuf.ByteString
        getBundleBytes() {
      java.lang.Object ref = bundle_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        bundle_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int F7_FIELD_NUMBER = 7;
    private CK.Item f7_;
    /**
     * <code>optional .Item f7 = 7;</code>
     */
    public boolean hasF7() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional .Item f7 = 7;</code>
     */
    public CK.Item getF7() {
      return f7_;
    }
    /**
     * <code>optional .Item f7 = 7;</code>
     */
    public CK.ItemOrBuilder getF7OrBuilder() {
      return f7_;
    }

    public static final int OS_FIELD_NUMBER = 8;
    private java.lang.Object os_;
    /**
     * <code>optional string os = 8;</code>
     */
    public boolean hasOs() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional string os = 8;</code>
     */
    public java.lang.String getOs() {
      java.lang.Object ref = os_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          os_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string os = 8;</code>
     */
    public com.google.protobuf.ByteString
        getOsBytes() {
      java.lang.Object ref = os_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        os_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int F9_FIELD_NUMBER = 9;
    private CK.Fixed64 f9_;
    /**
     * <code>optional .Fixed64 f9 = 9;</code>
     */
    public boolean hasF9() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    /**
     * <code>optional .Fixed64 f9 = 9;</code>
     */
    public CK.Fixed64 getF9() {
      return f9_;
    }
    /**
     * <code>optional .Fixed64 f9 = 9;</code>
     */
    public CK.Fixed64OrBuilder getF9OrBuilder() {
      return f9_;
    }

    public static final int APP_FIELD_NUMBER = 10;
    private java.lang.Object app_;
    /**
     * <code>optional string app = 10;</code>
     */
    public boolean hasApp() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    /**
     * <code>optional string app = 10;</code>
     */
    public java.lang.String getApp() {
      java.lang.Object ref = app_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          app_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string app = 10;</code>
     */
    public com.google.protobuf.ByteString
        getAppBytes() {
      java.lang.Object ref = app_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        app_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int APPVERSION_FIELD_NUMBER = 11;
    private java.lang.Object appVersion_;
    /**
     * <code>optional string appVersion = 11;</code>
     */
    public boolean hasAppVersion() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }
    /**
     * <code>optional string appVersion = 11;</code>
     */
    public java.lang.String getAppVersion() {
      java.lang.Object ref = appVersion_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          appVersion_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string appVersion = 11;</code>
     */
    public com.google.protobuf.ByteString
        getAppVersionBytes() {
      java.lang.Object ref = appVersion_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        appVersion_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int OPERATION_FIELD_NUMBER = 12;
    private java.lang.Object operation_;
    /**
     * <code>optional string operation = 12;</code>
     */
    public boolean hasOperation() {
      return ((bitField0_ & 0x00000080) == 0x00000080);
    }
    /**
     * <code>optional string operation = 12;</code>
     */
    public java.lang.String getOperation() {
      java.lang.Object ref = operation_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          operation_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string operation = 12;</code>
     */
    public com.google.protobuf.ByteString
        getOperationBytes() {
      java.lang.Object ref = operation_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        operation_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int LIMIT1_FIELD_NUMBER = 13;
    private int limit1_;
    /**
     * <code>optional uint32 limit1 = 13;</code>
     */
    public boolean hasLimit1() {
      return ((bitField0_ & 0x00000100) == 0x00000100);
    }
    /**
     * <code>optional uint32 limit1 = 13;</code>
     */
    public int getLimit1() {
      return limit1_;
    }

    public static final int LIMIT2_FIELD_NUMBER = 14;
    private int limit2_;
    /**
     * <code>optional uint32 limit2 = 14;</code>
     */
    public boolean hasLimit2() {
      return ((bitField0_ & 0x00000200) == 0x00000200);
    }
    /**
     * <code>optional uint32 limit2 = 14;</code>
     */
    public int getLimit2() {
      return limit2_;
    }

    public static final int HEX32_FIELD_NUMBER = 15;
    private int hex32_;
    /**
     * <code>optional fixed32 hex32 = 15;</code>
     */
    public boolean hasHex32() {
      return ((bitField0_ & 0x00000400) == 0x00000400);
    }
    /**
     * <code>optional fixed32 hex32 = 15;</code>
     */
    public int getHex32() {
      return hex32_;
    }

    public static final int VERSION_FIELD_NUMBER = 18;
    private java.lang.Object version_;
    /**
     * <code>optional string version = 18;</code>
     */
    public boolean hasVersion() {
      return ((bitField0_ & 0x00000800) == 0x00000800);
    }
    /**
     * <code>optional string version = 18;</code>
     */
    public java.lang.String getVersion() {
      java.lang.Object ref = version_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          version_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string version = 18;</code>
     */
    public com.google.protobuf.ByteString
        getVersionBytes() {
      java.lang.Object ref = version_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        version_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int F19_FIELD_NUMBER = 19;
    private int f19_;
    /**
     * <code>optional uint32 f19 = 19;</code>
     */
    public boolean hasF19() {
      return ((bitField0_ & 0x00001000) == 0x00001000);
    }
    /**
     * <code>optional uint32 f19 = 19;</code>
     */
    public int getF19() {
      return f19_;
    }

    public static final int DEVICENAME_FIELD_NUMBER = 21;
    private java.lang.Object deviceName_;
    /**
     * <code>optional string deviceName = 21;</code>
     */
    public boolean hasDeviceName() {
      return ((bitField0_ & 0x00002000) == 0x00002000);
    }
    /**
     * <code>optional string deviceName = 21;</code>
     */
    public java.lang.String getDeviceName() {
      java.lang.Object ref = deviceName_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          deviceName_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string deviceName = 21;</code>
     */
    public com.google.protobuf.ByteString
        getDeviceNameBytes() {
      java.lang.Object ref = deviceName_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        deviceName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int DEVICEID_FIELD_NUMBER = 22;
    private java.lang.Object deviceID_;
    /**
     * <code>optional string deviceID = 22;</code>
     */
    public boolean hasDeviceID() {
      return ((bitField0_ & 0x00004000) == 0x00004000);
    }
    /**
     * <code>optional string deviceID = 22;</code>
     */
    public java.lang.String getDeviceID() {
      java.lang.Object ref = deviceID_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          deviceID_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string deviceID = 22;</code>
     */
    public com.google.protobuf.ByteString
        getDeviceIDBytes() {
      java.lang.Object ref = deviceID_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        deviceID_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int F23_FIELD_NUMBER = 23;
    private int f23_;
    /**
     * <code>optional uint32 f23 = 23;</code>
     */
    public boolean hasF23() {
      return ((bitField0_ & 0x00008000) == 0x00008000);
    }
    /**
     * <code>optional uint32 f23 = 23;</code>
     */
    public int getF23() {
      return f23_;
    }

    public static final int F25_FIELD_NUMBER = 25;
    private int f25_;
    /**
     * <code>optional uint32 f25 = 25;</code>
     */
    public boolean hasF25() {
      return ((bitField0_ & 0x00010000) == 0x00010000);
    }
    /**
     * <code>optional uint32 f25 = 25;</code>
     */
    public int getF25() {
      return f25_;
    }

    private void initFields() {
      container_ = "";
      bundle_ = "";
      f7_ = CK.Item.getDefaultInstance();
      os_ = "";
      f9_ = CK.Fixed64.getDefaultInstance();
      app_ = "";
      appVersion_ = "";
      operation_ = "";
      limit1_ = 0;
      limit2_ = 0;
      hex32_ = 0;
      version_ = "";
      f19_ = 0;
      deviceName_ = "";
      deviceID_ = "";
      f23_ = 0;
      f25_ = 0;
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(2, getContainerBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(3, getBundleBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeMessage(7, f7_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBytes(8, getOsBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeMessage(9, f9_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeBytes(10, getAppBytes());
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeBytes(11, getAppVersionBytes());
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        output.writeBytes(12, getOperationBytes());
      }
      if (((bitField0_ & 0x00000100) == 0x00000100)) {
        output.writeUInt32(13, limit1_);
      }
      if (((bitField0_ & 0x00000200) == 0x00000200)) {
        output.writeUInt32(14, limit2_);
      }
      if (((bitField0_ & 0x00000400) == 0x00000400)) {
        output.writeFixed32(15, hex32_);
      }
      if (((bitField0_ & 0x00000800) == 0x00000800)) {
        output.writeBytes(18, getVersionBytes());
      }
      if (((bitField0_ & 0x00001000) == 0x00001000)) {
        output.writeUInt32(19, f19_);
      }
      if (((bitField0_ & 0x00002000) == 0x00002000)) {
        output.writeBytes(21, getDeviceNameBytes());
      }
      if (((bitField0_ & 0x00004000) == 0x00004000)) {
        output.writeBytes(22, getDeviceIDBytes());
      }
      if (((bitField0_ & 0x00008000) == 0x00008000)) {
        output.writeUInt32(23, f23_);
      }
      if (((bitField0_ & 0x00010000) == 0x00010000)) {
        output.writeUInt32(25, f25_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getContainerBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getBundleBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(7, f7_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(8, getOsBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(9, f9_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(10, getAppBytes());
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(11, getAppVersionBytes());
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(12, getOperationBytes());
      }
      if (((bitField0_ & 0x00000100) == 0x00000100)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(13, limit1_);
      }
      if (((bitField0_ & 0x00000200) == 0x00000200)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(14, limit2_);
      }
      if (((bitField0_ & 0x00000400) == 0x00000400)) {
        size += com.google.protobuf.CodedOutputStream
          .computeFixed32Size(15, hex32_);
      }
      if (((bitField0_ & 0x00000800) == 0x00000800)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(18, getVersionBytes());
      }
      if (((bitField0_ & 0x00001000) == 0x00001000)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(19, f19_);
      }
      if (((bitField0_ & 0x00002000) == 0x00002000)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(21, getDeviceNameBytes());
      }
      if (((bitField0_ & 0x00004000) == 0x00004000)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(22, getDeviceIDBytes());
      }
      if (((bitField0_ & 0x00008000) == 0x00008000)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(23, f23_);
      }
      if (((bitField0_ & 0x00010000) == 0x00010000)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(25, f25_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.Info parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Info parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Info parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Info parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Info parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Info parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.Info parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.Info parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.Info parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Info parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.Info prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code Info}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:Info)
        CK.InfoOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_Info_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_Info_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.Info.class, CK.Info.Builder.class);
      }

      // Construct using CloudKit.Info.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getF7FieldBuilder();
          getF9FieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        container_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        bundle_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        if (f7Builder_ == null) {
          f7_ = CK.Item.getDefaultInstance();
        } else {
          f7Builder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        os_ = "";
        bitField0_ = (bitField0_ & ~0x00000008);
        if (f9Builder_ == null) {
          f9_ = CK.Fixed64.getDefaultInstance();
        } else {
          f9Builder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        app_ = "";
        bitField0_ = (bitField0_ & ~0x00000020);
        appVersion_ = "";
        bitField0_ = (bitField0_ & ~0x00000040);
        operation_ = "";
        bitField0_ = (bitField0_ & ~0x00000080);
        limit1_ = 0;
        bitField0_ = (bitField0_ & ~0x00000100);
        limit2_ = 0;
        bitField0_ = (bitField0_ & ~0x00000200);
        hex32_ = 0;
        bitField0_ = (bitField0_ & ~0x00000400);
        version_ = "";
        bitField0_ = (bitField0_ & ~0x00000800);
        f19_ = 0;
        bitField0_ = (bitField0_ & ~0x00001000);
        deviceName_ = "";
        bitField0_ = (bitField0_ & ~0x00002000);
        deviceID_ = "";
        bitField0_ = (bitField0_ & ~0x00004000);
        f23_ = 0;
        bitField0_ = (bitField0_ & ~0x00008000);
        f25_ = 0;
        bitField0_ = (bitField0_ & ~0x00010000);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_Info_descriptor;
      }

      public CK.Info getDefaultInstanceForType() {
        return CK.Info.getDefaultInstance();
      }

      public CK.Info build() {
        CK.Info result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.Info buildPartial() {
        CK.Info result = new CK.Info(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.container_ = container_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.bundle_ = bundle_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        if (f7Builder_ == null) {
          result.f7_ = f7_;
        } else {
          result.f7_ = f7Builder_.build();
        }
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.os_ = os_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        if (f9Builder_ == null) {
          result.f9_ = f9_;
        } else {
          result.f9_ = f9Builder_.build();
        }
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.app_ = app_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000040;
        }
        result.appVersion_ = appVersion_;
        if (((from_bitField0_ & 0x00000080) == 0x00000080)) {
          to_bitField0_ |= 0x00000080;
        }
        result.operation_ = operation_;
        if (((from_bitField0_ & 0x00000100) == 0x00000100)) {
          to_bitField0_ |= 0x00000100;
        }
        result.limit1_ = limit1_;
        if (((from_bitField0_ & 0x00000200) == 0x00000200)) {
          to_bitField0_ |= 0x00000200;
        }
        result.limit2_ = limit2_;
        if (((from_bitField0_ & 0x00000400) == 0x00000400)) {
          to_bitField0_ |= 0x00000400;
        }
        result.hex32_ = hex32_;
        if (((from_bitField0_ & 0x00000800) == 0x00000800)) {
          to_bitField0_ |= 0x00000800;
        }
        result.version_ = version_;
        if (((from_bitField0_ & 0x00001000) == 0x00001000)) {
          to_bitField0_ |= 0x00001000;
        }
        result.f19_ = f19_;
        if (((from_bitField0_ & 0x00002000) == 0x00002000)) {
          to_bitField0_ |= 0x00002000;
        }
        result.deviceName_ = deviceName_;
        if (((from_bitField0_ & 0x00004000) == 0x00004000)) {
          to_bitField0_ |= 0x00004000;
        }
        result.deviceID_ = deviceID_;
        if (((from_bitField0_ & 0x00008000) == 0x00008000)) {
          to_bitField0_ |= 0x00008000;
        }
        result.f23_ = f23_;
        if (((from_bitField0_ & 0x00010000) == 0x00010000)) {
          to_bitField0_ |= 0x00010000;
        }
        result.f25_ = f25_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.Info) {
          return mergeFrom((CK.Info)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.Info other) {
        if (other == CK.Info.getDefaultInstance()) return this;
        if (other.hasContainer()) {
          bitField0_ |= 0x00000001;
          container_ = other.container_;
          onChanged();
        }
        if (other.hasBundle()) {
          bitField0_ |= 0x00000002;
          bundle_ = other.bundle_;
          onChanged();
        }
        if (other.hasF7()) {
          mergeF7(other.getF7());
        }
        if (other.hasOs()) {
          bitField0_ |= 0x00000008;
          os_ = other.os_;
          onChanged();
        }
        if (other.hasF9()) {
          mergeF9(other.getF9());
        }
        if (other.hasApp()) {
          bitField0_ |= 0x00000020;
          app_ = other.app_;
          onChanged();
        }
        if (other.hasAppVersion()) {
          bitField0_ |= 0x00000040;
          appVersion_ = other.appVersion_;
          onChanged();
        }
        if (other.hasOperation()) {
          bitField0_ |= 0x00000080;
          operation_ = other.operation_;
          onChanged();
        }
        if (other.hasLimit1()) {
          setLimit1(other.getLimit1());
        }
        if (other.hasLimit2()) {
          setLimit2(other.getLimit2());
        }
        if (other.hasHex32()) {
          setHex32(other.getHex32());
        }
        if (other.hasVersion()) {
          bitField0_ |= 0x00000800;
          version_ = other.version_;
          onChanged();
        }
        if (other.hasF19()) {
          setF19(other.getF19());
        }
        if (other.hasDeviceName()) {
          bitField0_ |= 0x00002000;
          deviceName_ = other.deviceName_;
          onChanged();
        }
        if (other.hasDeviceID()) {
          bitField0_ |= 0x00004000;
          deviceID_ = other.deviceID_;
          onChanged();
        }
        if (other.hasF23()) {
          setF23(other.getF23());
        }
        if (other.hasF25()) {
          setF25(other.getF25());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.Info parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.Info) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object container_ = "";
      /**
       * <code>optional string container = 2;</code>
       */
      public boolean hasContainer() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional string container = 2;</code>
       */
      public java.lang.String getContainer() {
        java.lang.Object ref = container_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            container_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string container = 2;</code>
       */
      public com.google.protobuf.ByteString
          getContainerBytes() {
        java.lang.Object ref = container_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          container_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string container = 2;</code>
       */
      public Builder setContainer(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        container_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string container = 2;</code>
       */
      public Builder clearContainer() {
        bitField0_ = (bitField0_ & ~0x00000001);
        container_ = getDefaultInstance().getContainer();
        onChanged();
        return this;
      }
      /**
       * <code>optional string container = 2;</code>
       */
      public Builder setContainerBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        container_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object bundle_ = "";
      /**
       * <code>optional string bundle = 3;</code>
       */
      public boolean hasBundle() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional string bundle = 3;</code>
       */
      public java.lang.String getBundle() {
        java.lang.Object ref = bundle_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            bundle_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string bundle = 3;</code>
       */
      public com.google.protobuf.ByteString
          getBundleBytes() {
        java.lang.Object ref = bundle_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          bundle_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string bundle = 3;</code>
       */
      public Builder setBundle(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        bundle_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string bundle = 3;</code>
       */
      public Builder clearBundle() {
        bitField0_ = (bitField0_ & ~0x00000002);
        bundle_ = getDefaultInstance().getBundle();
        onChanged();
        return this;
      }
      /**
       * <code>optional string bundle = 3;</code>
       */
      public Builder setBundleBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        bundle_ = value;
        onChanged();
        return this;
      }

      private CK.Item f7_ = CK.Item.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Item, CK.Item.Builder, CK.ItemOrBuilder> f7Builder_;
      /**
       * <code>optional .Item f7 = 7;</code>
       */
      public boolean hasF7() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional .Item f7 = 7;</code>
       */
      public CK.Item getF7() {
        if (f7Builder_ == null) {
          return f7_;
        } else {
          return f7Builder_.getMessage();
        }
      }
      /**
       * <code>optional .Item f7 = 7;</code>
       */
      public Builder setF7(CK.Item value) {
        if (f7Builder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          f7_ = value;
          onChanged();
        } else {
          f7Builder_.setMessage(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      /**
       * <code>optional .Item f7 = 7;</code>
       */
      public Builder setF7(
          CK.Item.Builder builderForValue) {
        if (f7Builder_ == null) {
          f7_ = builderForValue.build();
          onChanged();
        } else {
          f7Builder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      /**
       * <code>optional .Item f7 = 7;</code>
       */
      public Builder mergeF7(CK.Item value) {
        if (f7Builder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004) &&
              f7_ != CK.Item.getDefaultInstance()) {
            f7_ =
              CK.Item.newBuilder(f7_).mergeFrom(value).buildPartial();
          } else {
            f7_ = value;
          }
          onChanged();
        } else {
          f7Builder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      /**
       * <code>optional .Item f7 = 7;</code>
       */
      public Builder clearF7() {
        if (f7Builder_ == null) {
          f7_ = CK.Item.getDefaultInstance();
          onChanged();
        } else {
          f7Builder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      /**
       * <code>optional .Item f7 = 7;</code>
       */
      public CK.Item.Builder getF7Builder() {
        bitField0_ |= 0x00000004;
        onChanged();
        return getF7FieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Item f7 = 7;</code>
       */
      public CK.ItemOrBuilder getF7OrBuilder() {
        if (f7Builder_ != null) {
          return f7Builder_.getMessageOrBuilder();
        } else {
          return f7_;
        }
      }
      /**
       * <code>optional .Item f7 = 7;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Item, CK.Item.Builder, CK.ItemOrBuilder> 
          getF7FieldBuilder() {
        if (f7Builder_ == null) {
          f7Builder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Item, CK.Item.Builder, CK.ItemOrBuilder>(
                  getF7(),
                  getParentForChildren(),
                  isClean());
          f7_ = null;
        }
        return f7Builder_;
      }

      private java.lang.Object os_ = "";
      /**
       * <code>optional string os = 8;</code>
       */
      public boolean hasOs() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>optional string os = 8;</code>
       */
      public java.lang.String getOs() {
        java.lang.Object ref = os_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            os_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string os = 8;</code>
       */
      public com.google.protobuf.ByteString
          getOsBytes() {
        java.lang.Object ref = os_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          os_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string os = 8;</code>
       */
      public Builder setOs(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        os_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string os = 8;</code>
       */
      public Builder clearOs() {
        bitField0_ = (bitField0_ & ~0x00000008);
        os_ = getDefaultInstance().getOs();
        onChanged();
        return this;
      }
      /**
       * <code>optional string os = 8;</code>
       */
      public Builder setOsBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        os_ = value;
        onChanged();
        return this;
      }

      private CK.Fixed64 f9_ = CK.Fixed64.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Fixed64, CK.Fixed64.Builder, CK.Fixed64OrBuilder> f9Builder_;
      /**
       * <code>optional .Fixed64 f9 = 9;</code>
       */
      public boolean hasF9() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      /**
       * <code>optional .Fixed64 f9 = 9;</code>
       */
      public CK.Fixed64 getF9() {
        if (f9Builder_ == null) {
          return f9_;
        } else {
          return f9Builder_.getMessage();
        }
      }
      /**
       * <code>optional .Fixed64 f9 = 9;</code>
       */
      public Builder setF9(CK.Fixed64 value) {
        if (f9Builder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          f9_ = value;
          onChanged();
        } else {
          f9Builder_.setMessage(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      /**
       * <code>optional .Fixed64 f9 = 9;</code>
       */
      public Builder setF9(
          CK.Fixed64.Builder builderForValue) {
        if (f9Builder_ == null) {
          f9_ = builderForValue.build();
          onChanged();
        } else {
          f9Builder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      /**
       * <code>optional .Fixed64 f9 = 9;</code>
       */
      public Builder mergeF9(CK.Fixed64 value) {
        if (f9Builder_ == null) {
          if (((bitField0_ & 0x00000010) == 0x00000010) &&
              f9_ != CK.Fixed64.getDefaultInstance()) {
            f9_ =
              CK.Fixed64.newBuilder(f9_).mergeFrom(value).buildPartial();
          } else {
            f9_ = value;
          }
          onChanged();
        } else {
          f9Builder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      /**
       * <code>optional .Fixed64 f9 = 9;</code>
       */
      public Builder clearF9() {
        if (f9Builder_ == null) {
          f9_ = CK.Fixed64.getDefaultInstance();
          onChanged();
        } else {
          f9Builder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }
      /**
       * <code>optional .Fixed64 f9 = 9;</code>
       */
      public CK.Fixed64.Builder getF9Builder() {
        bitField0_ |= 0x00000010;
        onChanged();
        return getF9FieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Fixed64 f9 = 9;</code>
       */
      public CK.Fixed64OrBuilder getF9OrBuilder() {
        if (f9Builder_ != null) {
          return f9Builder_.getMessageOrBuilder();
        } else {
          return f9_;
        }
      }
      /**
       * <code>optional .Fixed64 f9 = 9;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Fixed64, CK.Fixed64.Builder, CK.Fixed64OrBuilder> 
          getF9FieldBuilder() {
        if (f9Builder_ == null) {
          f9Builder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Fixed64, CK.Fixed64.Builder, CK.Fixed64OrBuilder>(
                  getF9(),
                  getParentForChildren(),
                  isClean());
          f9_ = null;
        }
        return f9Builder_;
      }

      private java.lang.Object app_ = "";
      /**
       * <code>optional string app = 10;</code>
       */
      public boolean hasApp() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      /**
       * <code>optional string app = 10;</code>
       */
      public java.lang.String getApp() {
        java.lang.Object ref = app_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            app_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string app = 10;</code>
       */
      public com.google.protobuf.ByteString
          getAppBytes() {
        java.lang.Object ref = app_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          app_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string app = 10;</code>
       */
      public Builder setApp(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        app_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string app = 10;</code>
       */
      public Builder clearApp() {
        bitField0_ = (bitField0_ & ~0x00000020);
        app_ = getDefaultInstance().getApp();
        onChanged();
        return this;
      }
      /**
       * <code>optional string app = 10;</code>
       */
      public Builder setAppBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        app_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object appVersion_ = "";
      /**
       * <code>optional string appVersion = 11;</code>
       */
      public boolean hasAppVersion() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }
      /**
       * <code>optional string appVersion = 11;</code>
       */
      public java.lang.String getAppVersion() {
        java.lang.Object ref = appVersion_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            appVersion_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string appVersion = 11;</code>
       */
      public com.google.protobuf.ByteString
          getAppVersionBytes() {
        java.lang.Object ref = appVersion_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          appVersion_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string appVersion = 11;</code>
       */
      public Builder setAppVersion(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000040;
        appVersion_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string appVersion = 11;</code>
       */
      public Builder clearAppVersion() {
        bitField0_ = (bitField0_ & ~0x00000040);
        appVersion_ = getDefaultInstance().getAppVersion();
        onChanged();
        return this;
      }
      /**
       * <code>optional string appVersion = 11;</code>
       */
      public Builder setAppVersionBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000040;
        appVersion_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object operation_ = "";
      /**
       * <code>optional string operation = 12;</code>
       */
      public boolean hasOperation() {
        return ((bitField0_ & 0x00000080) == 0x00000080);
      }
      /**
       * <code>optional string operation = 12;</code>
       */
      public java.lang.String getOperation() {
        java.lang.Object ref = operation_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            operation_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string operation = 12;</code>
       */
      public com.google.protobuf.ByteString
          getOperationBytes() {
        java.lang.Object ref = operation_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          operation_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string operation = 12;</code>
       */
      public Builder setOperation(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000080;
        operation_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string operation = 12;</code>
       */
      public Builder clearOperation() {
        bitField0_ = (bitField0_ & ~0x00000080);
        operation_ = getDefaultInstance().getOperation();
        onChanged();
        return this;
      }
      /**
       * <code>optional string operation = 12;</code>
       */
      public Builder setOperationBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000080;
        operation_ = value;
        onChanged();
        return this;
      }

      private int limit1_ ;
      /**
       * <code>optional uint32 limit1 = 13;</code>
       */
      public boolean hasLimit1() {
        return ((bitField0_ & 0x00000100) == 0x00000100);
      }
      /**
       * <code>optional uint32 limit1 = 13;</code>
       */
      public int getLimit1() {
        return limit1_;
      }
      /**
       * <code>optional uint32 limit1 = 13;</code>
       */
      public Builder setLimit1(int value) {
        bitField0_ |= 0x00000100;
        limit1_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 limit1 = 13;</code>
       */
      public Builder clearLimit1() {
        bitField0_ = (bitField0_ & ~0x00000100);
        limit1_ = 0;
        onChanged();
        return this;
      }

      private int limit2_ ;
      /**
       * <code>optional uint32 limit2 = 14;</code>
       */
      public boolean hasLimit2() {
        return ((bitField0_ & 0x00000200) == 0x00000200);
      }
      /**
       * <code>optional uint32 limit2 = 14;</code>
       */
      public int getLimit2() {
        return limit2_;
      }
      /**
       * <code>optional uint32 limit2 = 14;</code>
       */
      public Builder setLimit2(int value) {
        bitField0_ |= 0x00000200;
        limit2_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 limit2 = 14;</code>
       */
      public Builder clearLimit2() {
        bitField0_ = (bitField0_ & ~0x00000200);
        limit2_ = 0;
        onChanged();
        return this;
      }

      private int hex32_ ;
      /**
       * <code>optional fixed32 hex32 = 15;</code>
       */
      public boolean hasHex32() {
        return ((bitField0_ & 0x00000400) == 0x00000400);
      }
      /**
       * <code>optional fixed32 hex32 = 15;</code>
       */
      public int getHex32() {
        return hex32_;
      }
      /**
       * <code>optional fixed32 hex32 = 15;</code>
       */
      public Builder setHex32(int value) {
        bitField0_ |= 0x00000400;
        hex32_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional fixed32 hex32 = 15;</code>
       */
      public Builder clearHex32() {
        bitField0_ = (bitField0_ & ~0x00000400);
        hex32_ = 0;
        onChanged();
        return this;
      }

      private java.lang.Object version_ = "";
      /**
       * <code>optional string version = 18;</code>
       */
      public boolean hasVersion() {
        return ((bitField0_ & 0x00000800) == 0x00000800);
      }
      /**
       * <code>optional string version = 18;</code>
       */
      public java.lang.String getVersion() {
        java.lang.Object ref = version_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            version_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string version = 18;</code>
       */
      public com.google.protobuf.ByteString
          getVersionBytes() {
        java.lang.Object ref = version_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          version_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string version = 18;</code>
       */
      public Builder setVersion(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000800;
        version_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string version = 18;</code>
       */
      public Builder clearVersion() {
        bitField0_ = (bitField0_ & ~0x00000800);
        version_ = getDefaultInstance().getVersion();
        onChanged();
        return this;
      }
      /**
       * <code>optional string version = 18;</code>
       */
      public Builder setVersionBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000800;
        version_ = value;
        onChanged();
        return this;
      }

      private int f19_ ;
      /**
       * <code>optional uint32 f19 = 19;</code>
       */
      public boolean hasF19() {
        return ((bitField0_ & 0x00001000) == 0x00001000);
      }
      /**
       * <code>optional uint32 f19 = 19;</code>
       */
      public int getF19() {
        return f19_;
      }
      /**
       * <code>optional uint32 f19 = 19;</code>
       */
      public Builder setF19(int value) {
        bitField0_ |= 0x00001000;
        f19_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 f19 = 19;</code>
       */
      public Builder clearF19() {
        bitField0_ = (bitField0_ & ~0x00001000);
        f19_ = 0;
        onChanged();
        return this;
      }

      private java.lang.Object deviceName_ = "";
      /**
       * <code>optional string deviceName = 21;</code>
       */
      public boolean hasDeviceName() {
        return ((bitField0_ & 0x00002000) == 0x00002000);
      }
      /**
       * <code>optional string deviceName = 21;</code>
       */
      public java.lang.String getDeviceName() {
        java.lang.Object ref = deviceName_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            deviceName_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string deviceName = 21;</code>
       */
      public com.google.protobuf.ByteString
          getDeviceNameBytes() {
        java.lang.Object ref = deviceName_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          deviceName_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string deviceName = 21;</code>
       */
      public Builder setDeviceName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00002000;
        deviceName_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string deviceName = 21;</code>
       */
      public Builder clearDeviceName() {
        bitField0_ = (bitField0_ & ~0x00002000);
        deviceName_ = getDefaultInstance().getDeviceName();
        onChanged();
        return this;
      }
      /**
       * <code>optional string deviceName = 21;</code>
       */
      public Builder setDeviceNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00002000;
        deviceName_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object deviceID_ = "";
      /**
       * <code>optional string deviceID = 22;</code>
       */
      public boolean hasDeviceID() {
        return ((bitField0_ & 0x00004000) == 0x00004000);
      }
      /**
       * <code>optional string deviceID = 22;</code>
       */
      public java.lang.String getDeviceID() {
        java.lang.Object ref = deviceID_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            deviceID_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string deviceID = 22;</code>
       */
      public com.google.protobuf.ByteString
          getDeviceIDBytes() {
        java.lang.Object ref = deviceID_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          deviceID_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string deviceID = 22;</code>
       */
      public Builder setDeviceID(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00004000;
        deviceID_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string deviceID = 22;</code>
       */
      public Builder clearDeviceID() {
        bitField0_ = (bitField0_ & ~0x00004000);
        deviceID_ = getDefaultInstance().getDeviceID();
        onChanged();
        return this;
      }
      /**
       * <code>optional string deviceID = 22;</code>
       */
      public Builder setDeviceIDBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00004000;
        deviceID_ = value;
        onChanged();
        return this;
      }

      private int f23_ ;
      /**
       * <code>optional uint32 f23 = 23;</code>
       */
      public boolean hasF23() {
        return ((bitField0_ & 0x00008000) == 0x00008000);
      }
      /**
       * <code>optional uint32 f23 = 23;</code>
       */
      public int getF23() {
        return f23_;
      }
      /**
       * <code>optional uint32 f23 = 23;</code>
       */
      public Builder setF23(int value) {
        bitField0_ |= 0x00008000;
        f23_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 f23 = 23;</code>
       */
      public Builder clearF23() {
        bitField0_ = (bitField0_ & ~0x00008000);
        f23_ = 0;
        onChanged();
        return this;
      }

      private int f25_ ;
      /**
       * <code>optional uint32 f25 = 25;</code>
       */
      public boolean hasF25() {
        return ((bitField0_ & 0x00010000) == 0x00010000);
      }
      /**
       * <code>optional uint32 f25 = 25;</code>
       */
      public int getF25() {
        return f25_;
      }
      /**
       * <code>optional uint32 f25 = 25;</code>
       */
      public Builder setF25(int value) {
        bitField0_ |= 0x00010000;
        f25_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 f25 = 25;</code>
       */
      public Builder clearF25() {
        bitField0_ = (bitField0_ & ~0x00010000);
        f25_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:Info)
    }

    static {
      defaultInstance = new Info(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:Info)
  }

  public interface MessageOrBuilder extends
      // @@protoc_insertion_point(interface_extends:Message)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string uuid = 1;</code>
     */
    boolean hasUuid();
    /**
     * <code>optional string uuid = 1;</code>
     */
    java.lang.String getUuid();
    /**
     * <code>optional string uuid = 1;</code>
     */
    com.google.protobuf.ByteString
        getUuidBytes();

    /**
     * <code>optional uint32 type = 2;</code>
     */
    boolean hasType();
    /**
     * <code>optional uint32 type = 2;</code>
     */
    int getType();

    /**
     * <code>optional uint32 f4 = 4;</code>
     */
    boolean hasF4();
    /**
     * <code>optional uint32 f4 = 4;</code>
     */
    int getF4();
  }
  /**
   * Protobuf type {@code Message}
   */
  public static final class Message extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:Message)
      MessageOrBuilder {
    // Use Message.newBuilder() to construct.
    private Message(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Message(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Message defaultInstance;
    public static Message getDefaultInstance() {
      return defaultInstance;
    }

    public Message getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Message(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
              uuid_ = bs;
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              type_ = input.readUInt32();
              break;
            }
            case 32: {
              bitField0_ |= 0x00000004;
              f4_ = input.readUInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_Message_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_Message_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.Message.class, CK.Message.Builder.class);
    }

    public static com.google.protobuf.Parser<Message> PARSER =
        new com.google.protobuf.AbstractParser<Message>() {
      public Message parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Message(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Message> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int UUID_FIELD_NUMBER = 1;
    private java.lang.Object uuid_;
    /**
     * <code>optional string uuid = 1;</code>
     */
    public boolean hasUuid() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional string uuid = 1;</code>
     */
    public java.lang.String getUuid() {
      java.lang.Object ref = uuid_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          uuid_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string uuid = 1;</code>
     */
    public com.google.protobuf.ByteString
        getUuidBytes() {
      java.lang.Object ref = uuid_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        uuid_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int TYPE_FIELD_NUMBER = 2;
    private int type_;
    /**
     * <code>optional uint32 type = 2;</code>
     */
    public boolean hasType() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional uint32 type = 2;</code>
     */
    public int getType() {
      return type_;
    }

    public static final int F4_FIELD_NUMBER = 4;
    private int f4_;
    /**
     * <code>optional uint32 f4 = 4;</code>
     */
    public boolean hasF4() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional uint32 f4 = 4;</code>
     */
    public int getF4() {
      return f4_;
    }

    private void initFields() {
      uuid_ = "";
      type_ = 0;
      f4_ = 0;
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getUuidBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeUInt32(2, type_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeUInt32(4, f4_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getUuidBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(2, type_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(4, f4_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.Message parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Message parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Message parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Message parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Message parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Message parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.Message parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.Message parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.Message parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Message parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.Message prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code Message}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:Message)
        CK.MessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_Message_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_Message_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.Message.class, CK.Message.Builder.class);
      }

      // Construct using CloudKit.Message.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        uuid_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        type_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        f4_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_Message_descriptor;
      }

      public CK.Message getDefaultInstanceForType() {
        return CK.Message.getDefaultInstance();
      }

      public CK.Message build() {
        CK.Message result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.Message buildPartial() {
        CK.Message result = new CK.Message(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.uuid_ = uuid_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.type_ = type_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.f4_ = f4_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.Message) {
          return mergeFrom((CK.Message)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.Message other) {
        if (other == CK.Message.getDefaultInstance()) return this;
        if (other.hasUuid()) {
          bitField0_ |= 0x00000001;
          uuid_ = other.uuid_;
          onChanged();
        }
        if (other.hasType()) {
          setType(other.getType());
        }
        if (other.hasF4()) {
          setF4(other.getF4());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.Message parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.Message) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object uuid_ = "";
      /**
       * <code>optional string uuid = 1;</code>
       */
      public boolean hasUuid() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional string uuid = 1;</code>
       */
      public java.lang.String getUuid() {
        java.lang.Object ref = uuid_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            uuid_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string uuid = 1;</code>
       */
      public com.google.protobuf.ByteString
          getUuidBytes() {
        java.lang.Object ref = uuid_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          uuid_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string uuid = 1;</code>
       */
      public Builder setUuid(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        uuid_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string uuid = 1;</code>
       */
      public Builder clearUuid() {
        bitField0_ = (bitField0_ & ~0x00000001);
        uuid_ = getDefaultInstance().getUuid();
        onChanged();
        return this;
      }
      /**
       * <code>optional string uuid = 1;</code>
       */
      public Builder setUuidBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        uuid_ = value;
        onChanged();
        return this;
      }

      private int type_ ;
      /**
       * <code>optional uint32 type = 2;</code>
       */
      public boolean hasType() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional uint32 type = 2;</code>
       */
      public int getType() {
        return type_;
      }
      /**
       * <code>optional uint32 type = 2;</code>
       */
      public Builder setType(int value) {
        bitField0_ |= 0x00000002;
        type_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 type = 2;</code>
       */
      public Builder clearType() {
        bitField0_ = (bitField0_ & ~0x00000002);
        type_ = 0;
        onChanged();
        return this;
      }

      private int f4_ ;
      /**
       * <code>optional uint32 f4 = 4;</code>
       */
      public boolean hasF4() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional uint32 f4 = 4;</code>
       */
      public int getF4() {
        return f4_;
      }
      /**
       * <code>optional uint32 f4 = 4;</code>
       */
      public Builder setF4(int value) {
        bitField0_ |= 0x00000004;
        f4_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 f4 = 4;</code>
       */
      public Builder clearF4() {
        bitField0_ = (bitField0_ & ~0x00000004);
        f4_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:Message)
    }

    static {
      defaultInstance = new Message(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:Message)
  }

  public interface M201RequestOrBuilder extends
      // @@protoc_insertion_point(interface_extends:M201Request)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional .Op op = 1;</code>
     */
    boolean hasOp();
    /**
     * <code>optional .Op op = 1;</code>
     */
    CK.Op getOp();
    /**
     * <code>optional .Op op = 1;</code>
     */
    CK.OpOrBuilder getOpOrBuilder();
  }
  /**
   * Protobuf type {@code M201Request}
   */
  public static final class M201Request extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:M201Request)
      M201RequestOrBuilder {
    // Use M201Request.newBuilder() to construct.
    private M201Request(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private M201Request(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final M201Request defaultInstance;
    public static M201Request getDefaultInstance() {
      return defaultInstance;
    }

    public M201Request getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private M201Request(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
              CK.Op.Builder subBuilder = null;
              if (((bitField0_ & 0x00000001) == 0x00000001)) {
                subBuilder = op_.toBuilder();
              }
              op_ = input.readMessage(CK.Op.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(op_);
                op_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000001;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_M201Request_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_M201Request_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.M201Request.class, CK.M201Request.Builder.class);
    }

    public static com.google.protobuf.Parser<M201Request> PARSER =
        new com.google.protobuf.AbstractParser<M201Request>() {
      public M201Request parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new M201Request(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<M201Request> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int OP_FIELD_NUMBER = 1;
    private CK.Op op_;
    /**
     * <code>optional .Op op = 1;</code>
     */
    public boolean hasOp() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional .Op op = 1;</code>
     */
    public CK.Op getOp() {
      return op_;
    }
    /**
     * <code>optional .Op op = 1;</code>
     */
    public CK.OpOrBuilder getOpOrBuilder() {
      return op_;
    }

    private void initFields() {
      op_ = CK.Op.getDefaultInstance();
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeMessage(1, op_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, op_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.M201Request parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.M201Request parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.M201Request parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.M201Request parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.M201Request parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.M201Request parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.M201Request parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.M201Request parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.M201Request parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.M201Request parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.M201Request prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code M201Request}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:M201Request)
        CK.M201RequestOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_M201Request_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_M201Request_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.M201Request.class, CK.M201Request.Builder.class);
      }

      // Construct using CloudKit.M201Request.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getOpFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (opBuilder_ == null) {
          op_ = CK.Op.getDefaultInstance();
        } else {
          opBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_M201Request_descriptor;
      }

      public CK.M201Request getDefaultInstanceForType() {
        return CK.M201Request.getDefaultInstance();
      }

      public CK.M201Request build() {
        CK.M201Request result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.M201Request buildPartial() {
        CK.M201Request result = new CK.M201Request(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (opBuilder_ == null) {
          result.op_ = op_;
        } else {
          result.op_ = opBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.M201Request) {
          return mergeFrom((CK.M201Request)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.M201Request other) {
        if (other == CK.M201Request.getDefaultInstance()) return this;
        if (other.hasOp()) {
          mergeOp(other.getOp());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.M201Request parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.M201Request) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private CK.Op op_ = CK.Op.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Op, CK.Op.Builder, CK.OpOrBuilder> opBuilder_;
      /**
       * <code>optional .Op op = 1;</code>
       */
      public boolean hasOp() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional .Op op = 1;</code>
       */
      public CK.Op getOp() {
        if (opBuilder_ == null) {
          return op_;
        } else {
          return opBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .Op op = 1;</code>
       */
      public Builder setOp(CK.Op value) {
        if (opBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          op_ = value;
          onChanged();
        } else {
          opBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .Op op = 1;</code>
       */
      public Builder setOp(
          CK.Op.Builder builderForValue) {
        if (opBuilder_ == null) {
          op_ = builderForValue.build();
          onChanged();
        } else {
          opBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .Op op = 1;</code>
       */
      public Builder mergeOp(CK.Op value) {
        if (opBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              op_ != CK.Op.getDefaultInstance()) {
            op_ =
              CK.Op.newBuilder(op_).mergeFrom(value).buildPartial();
          } else {
            op_ = value;
          }
          onChanged();
        } else {
          opBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .Op op = 1;</code>
       */
      public Builder clearOp() {
        if (opBuilder_ == null) {
          op_ = CK.Op.getDefaultInstance();
          onChanged();
        } else {
          opBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      /**
       * <code>optional .Op op = 1;</code>
       */
      public CK.Op.Builder getOpBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getOpFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Op op = 1;</code>
       */
      public CK.OpOrBuilder getOpOrBuilder() {
        if (opBuilder_ != null) {
          return opBuilder_.getMessageOrBuilder();
        } else {
          return op_;
        }
      }
      /**
       * <code>optional .Op op = 1;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Op, CK.Op.Builder, CK.OpOrBuilder> 
          getOpFieldBuilder() {
        if (opBuilder_ == null) {
          opBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Op, CK.Op.Builder, CK.OpOrBuilder>(
                  getOp(),
                  getParentForChildren(),
                  isClean());
          op_ = null;
        }
        return opBuilder_;
      }

      // @@protoc_insertion_point(builder_scope:M201Request)
    }

    static {
      defaultInstance = new M201Request(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:M201Request)
  }

  public interface M201ResponseOrBuilder extends
      // @@protoc_insertion_point(interface_extends:M201Response)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional .M201ResponseBody body = 1;</code>
     */
    boolean hasBody();
    /**
     * <code>optional .M201ResponseBody body = 1;</code>
     */
    CK.M201ResponseBody getBody();
    /**
     * <code>optional .M201ResponseBody body = 1;</code>
     */
    CK.M201ResponseBodyOrBuilder getBodyOrBuilder();
  }
  /**
   * Protobuf type {@code M201Response}
   */
  public static final class M201Response extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:M201Response)
      M201ResponseOrBuilder {
    // Use M201Response.newBuilder() to construct.
    private M201Response(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private M201Response(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final M201Response defaultInstance;
    public static M201Response getDefaultInstance() {
      return defaultInstance;
    }

    public M201Response getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private M201Response(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
              CK.M201ResponseBody.Builder subBuilder = null;
              if (((bitField0_ & 0x00000001) == 0x00000001)) {
                subBuilder = body_.toBuilder();
              }
              body_ = input.readMessage(CK.M201ResponseBody.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(body_);
                body_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000001;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_M201Response_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_M201Response_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.M201Response.class, CK.M201Response.Builder.class);
    }

    public static com.google.protobuf.Parser<M201Response> PARSER =
        new com.google.protobuf.AbstractParser<M201Response>() {
      public M201Response parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new M201Response(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<M201Response> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int BODY_FIELD_NUMBER = 1;
    private CK.M201ResponseBody body_;
    /**
     * <code>optional .M201ResponseBody body = 1;</code>
     */
    public boolean hasBody() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional .M201ResponseBody body = 1;</code>
     */
    public CK.M201ResponseBody getBody() {
      return body_;
    }
    /**
     * <code>optional .M201ResponseBody body = 1;</code>
     */
    public CK.M201ResponseBodyOrBuilder getBodyOrBuilder() {
      return body_;
    }

    private void initFields() {
      body_ = CK.M201ResponseBody.getDefaultInstance();
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeMessage(1, body_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, body_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.M201Response parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.M201Response parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.M201Response parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.M201Response parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.M201Response parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.M201Response parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.M201Response parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.M201Response parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.M201Response parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.M201Response parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.M201Response prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code M201Response}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:M201Response)
        CK.M201ResponseOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_M201Response_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_M201Response_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.M201Response.class, CK.M201Response.Builder.class);
      }

      // Construct using CloudKit.M201Response.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getBodyFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (bodyBuilder_ == null) {
          body_ = CK.M201ResponseBody.getDefaultInstance();
        } else {
          bodyBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_M201Response_descriptor;
      }

      public CK.M201Response getDefaultInstanceForType() {
        return CK.M201Response.getDefaultInstance();
      }

      public CK.M201Response build() {
        CK.M201Response result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.M201Response buildPartial() {
        CK.M201Response result = new CK.M201Response(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (bodyBuilder_ == null) {
          result.body_ = body_;
        } else {
          result.body_ = bodyBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.M201Response) {
          return mergeFrom((CK.M201Response)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.M201Response other) {
        if (other == CK.M201Response.getDefaultInstance()) return this;
        if (other.hasBody()) {
          mergeBody(other.getBody());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.M201Response parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.M201Response) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private CK.M201ResponseBody body_ = CK.M201ResponseBody.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.M201ResponseBody, CK.M201ResponseBody.Builder, CK.M201ResponseBodyOrBuilder> bodyBuilder_;
      /**
       * <code>optional .M201ResponseBody body = 1;</code>
       */
      public boolean hasBody() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional .M201ResponseBody body = 1;</code>
       */
      public CK.M201ResponseBody getBody() {
        if (bodyBuilder_ == null) {
          return body_;
        } else {
          return bodyBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .M201ResponseBody body = 1;</code>
       */
      public Builder setBody(CK.M201ResponseBody value) {
        if (bodyBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          body_ = value;
          onChanged();
        } else {
          bodyBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .M201ResponseBody body = 1;</code>
       */
      public Builder setBody(
          CK.M201ResponseBody.Builder builderForValue) {
        if (bodyBuilder_ == null) {
          body_ = builderForValue.build();
          onChanged();
        } else {
          bodyBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .M201ResponseBody body = 1;</code>
       */
      public Builder mergeBody(CK.M201ResponseBody value) {
        if (bodyBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              body_ != CK.M201ResponseBody.getDefaultInstance()) {
            body_ =
              CK.M201ResponseBody.newBuilder(body_).mergeFrom(value).buildPartial();
          } else {
            body_ = value;
          }
          onChanged();
        } else {
          bodyBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .M201ResponseBody body = 1;</code>
       */
      public Builder clearBody() {
        if (bodyBuilder_ == null) {
          body_ = CK.M201ResponseBody.getDefaultInstance();
          onChanged();
        } else {
          bodyBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      /**
       * <code>optional .M201ResponseBody body = 1;</code>
       */
      public CK.M201ResponseBody.Builder getBodyBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getBodyFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .M201ResponseBody body = 1;</code>
       */
      public CK.M201ResponseBodyOrBuilder getBodyOrBuilder() {
        if (bodyBuilder_ != null) {
          return bodyBuilder_.getMessageOrBuilder();
        } else {
          return body_;
        }
      }
      /**
       * <code>optional .M201ResponseBody body = 1;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.M201ResponseBody, CK.M201ResponseBody.Builder, CK.M201ResponseBodyOrBuilder> 
          getBodyFieldBuilder() {
        if (bodyBuilder_ == null) {
          bodyBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.M201ResponseBody, CK.M201ResponseBody.Builder, CK.M201ResponseBodyOrBuilder>(
                  getBody(),
                  getParentForChildren(),
                  isClean());
          body_ = null;
        }
        return bodyBuilder_;
      }

      // @@protoc_insertion_point(builder_scope:M201Response)
    }

    static {
      defaultInstance = new M201Response(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:M201Response)
  }

  public interface M201ResponseBodyOrBuilder extends
      // @@protoc_insertion_point(interface_extends:M201ResponseBody)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional .OpResult result = 1;</code>
     */
    boolean hasResult();
    /**
     * <code>optional .OpResult result = 1;</code>
     */
    CK.OpResult getResult();
    /**
     * <code>optional .OpResult result = 1;</code>
     */
    CK.OpResultOrBuilder getResultOrBuilder();

    /**
     * <code>optional bytes f2 = 2;</code>
     */
    boolean hasF2();
    /**
     * <code>optional bytes f2 = 2;</code>
     */
    com.google.protobuf.ByteString getF2();

    /**
     * <code>optional uint32 f4 = 4;</code>
     */
    boolean hasF4();
    /**
     * <code>optional uint32 f4 = 4;</code>
     */
    int getF4();

    /**
     * <code>optional uint32 f5 = 5;</code>
     */
    boolean hasF5();
    /**
     * <code>optional uint32 f5 = 5;</code>
     */
    int getF5();

    /**
     * <code>optional uint32 f6 = 6;</code>
     */
    boolean hasF6();
    /**
     * <code>optional uint32 f6 = 6;</code>
     */
    int getF6();
  }
  /**
   * Protobuf type {@code M201ResponseBody}
   */
  public static final class M201ResponseBody extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:M201ResponseBody)
      M201ResponseBodyOrBuilder {
    // Use M201ResponseBody.newBuilder() to construct.
    private M201ResponseBody(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private M201ResponseBody(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final M201ResponseBody defaultInstance;
    public static M201ResponseBody getDefaultInstance() {
      return defaultInstance;
    }

    public M201ResponseBody getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private M201ResponseBody(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
              CK.OpResult.Builder subBuilder = null;
              if (((bitField0_ & 0x00000001) == 0x00000001)) {
                subBuilder = result_.toBuilder();
              }
              result_ = input.readMessage(CK.OpResult.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(result_);
                result_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000001;
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              f2_ = input.readBytes();
              break;
            }
            case 32: {
              bitField0_ |= 0x00000004;
              f4_ = input.readUInt32();
              break;
            }
            case 40: {
              bitField0_ |= 0x00000008;
              f5_ = input.readUInt32();
              break;
            }
            case 48: {
              bitField0_ |= 0x00000010;
              f6_ = input.readUInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_M201ResponseBody_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_M201ResponseBody_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.M201ResponseBody.class, CK.M201ResponseBody.Builder.class);
    }

    public static com.google.protobuf.Parser<M201ResponseBody> PARSER =
        new com.google.protobuf.AbstractParser<M201ResponseBody>() {
      public M201ResponseBody parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new M201ResponseBody(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<M201ResponseBody> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int RESULT_FIELD_NUMBER = 1;
    private CK.OpResult result_;
    /**
     * <code>optional .OpResult result = 1;</code>
     */
    public boolean hasResult() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional .OpResult result = 1;</code>
     */
    public CK.OpResult getResult() {
      return result_;
    }
    /**
     * <code>optional .OpResult result = 1;</code>
     */
    public CK.OpResultOrBuilder getResultOrBuilder() {
      return result_;
    }

    public static final int F2_FIELD_NUMBER = 2;
    private com.google.protobuf.ByteString f2_;
    /**
     * <code>optional bytes f2 = 2;</code>
     */
    public boolean hasF2() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional bytes f2 = 2;</code>
     */
    public com.google.protobuf.ByteString getF2() {
      return f2_;
    }

    public static final int F4_FIELD_NUMBER = 4;
    private int f4_;
    /**
     * <code>optional uint32 f4 = 4;</code>
     */
    public boolean hasF4() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional uint32 f4 = 4;</code>
     */
    public int getF4() {
      return f4_;
    }

    public static final int F5_FIELD_NUMBER = 5;
    private int f5_;
    /**
     * <code>optional uint32 f5 = 5;</code>
     */
    public boolean hasF5() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional uint32 f5 = 5;</code>
     */
    public int getF5() {
      return f5_;
    }

    public static final int F6_FIELD_NUMBER = 6;
    private int f6_;
    /**
     * <code>optional uint32 f6 = 6;</code>
     */
    public boolean hasF6() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    /**
     * <code>optional uint32 f6 = 6;</code>
     */
    public int getF6() {
      return f6_;
    }

    private void initFields() {
      result_ = CK.OpResult.getDefaultInstance();
      f2_ = com.google.protobuf.ByteString.EMPTY;
      f4_ = 0;
      f5_ = 0;
      f6_ = 0;
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeMessage(1, result_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, f2_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeUInt32(4, f4_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeUInt32(5, f5_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeUInt32(6, f6_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, result_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, f2_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(4, f4_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(5, f5_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(6, f6_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.M201ResponseBody parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.M201ResponseBody parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.M201ResponseBody parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.M201ResponseBody parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.M201ResponseBody parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.M201ResponseBody parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.M201ResponseBody parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.M201ResponseBody parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.M201ResponseBody parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.M201ResponseBody parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.M201ResponseBody prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code M201ResponseBody}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:M201ResponseBody)
        CK.M201ResponseBodyOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_M201ResponseBody_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_M201ResponseBody_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.M201ResponseBody.class, CK.M201ResponseBody.Builder.class);
      }

      // Construct using CloudKit.M201ResponseBody.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getResultFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (resultBuilder_ == null) {
          result_ = CK.OpResult.getDefaultInstance();
        } else {
          resultBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        f2_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000002);
        f4_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        f5_ = 0;
        bitField0_ = (bitField0_ & ~0x00000008);
        f6_ = 0;
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_M201ResponseBody_descriptor;
      }

      public CK.M201ResponseBody getDefaultInstanceForType() {
        return CK.M201ResponseBody.getDefaultInstance();
      }

      public CK.M201ResponseBody build() {
        CK.M201ResponseBody result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.M201ResponseBody buildPartial() {
        CK.M201ResponseBody result = new CK.M201ResponseBody(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (resultBuilder_ == null) {
          result.result_ = result_;
        } else {
          result.result_ = resultBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.f2_ = f2_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.f4_ = f4_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.f5_ = f5_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.f6_ = f6_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.M201ResponseBody) {
          return mergeFrom((CK.M201ResponseBody)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.M201ResponseBody other) {
        if (other == CK.M201ResponseBody.getDefaultInstance()) return this;
        if (other.hasResult()) {
          mergeResult(other.getResult());
        }
        if (other.hasF2()) {
          setF2(other.getF2());
        }
        if (other.hasF4()) {
          setF4(other.getF4());
        }
        if (other.hasF5()) {
          setF5(other.getF5());
        }
        if (other.hasF6()) {
          setF6(other.getF6());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.M201ResponseBody parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.M201ResponseBody) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private CK.OpResult result_ = CK.OpResult.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.OpResult, CK.OpResult.Builder, CK.OpResultOrBuilder> resultBuilder_;
      /**
       * <code>optional .OpResult result = 1;</code>
       */
      public boolean hasResult() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional .OpResult result = 1;</code>
       */
      public CK.OpResult getResult() {
        if (resultBuilder_ == null) {
          return result_;
        } else {
          return resultBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .OpResult result = 1;</code>
       */
      public Builder setResult(CK.OpResult value) {
        if (resultBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          result_ = value;
          onChanged();
        } else {
          resultBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .OpResult result = 1;</code>
       */
      public Builder setResult(
          CK.OpResult.Builder builderForValue) {
        if (resultBuilder_ == null) {
          result_ = builderForValue.build();
          onChanged();
        } else {
          resultBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .OpResult result = 1;</code>
       */
      public Builder mergeResult(CK.OpResult value) {
        if (resultBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              result_ != CK.OpResult.getDefaultInstance()) {
            result_ =
              CK.OpResult.newBuilder(result_).mergeFrom(value).buildPartial();
          } else {
            result_ = value;
          }
          onChanged();
        } else {
          resultBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .OpResult result = 1;</code>
       */
      public Builder clearResult() {
        if (resultBuilder_ == null) {
          result_ = CK.OpResult.getDefaultInstance();
          onChanged();
        } else {
          resultBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      /**
       * <code>optional .OpResult result = 1;</code>
       */
      public CK.OpResult.Builder getResultBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getResultFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .OpResult result = 1;</code>
       */
      public CK.OpResultOrBuilder getResultOrBuilder() {
        if (resultBuilder_ != null) {
          return resultBuilder_.getMessageOrBuilder();
        } else {
          return result_;
        }
      }
      /**
       * <code>optional .OpResult result = 1;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.OpResult, CK.OpResult.Builder, CK.OpResultOrBuilder> 
          getResultFieldBuilder() {
        if (resultBuilder_ == null) {
          resultBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.OpResult, CK.OpResult.Builder, CK.OpResultOrBuilder>(
                  getResult(),
                  getParentForChildren(),
                  isClean());
          result_ = null;
        }
        return resultBuilder_;
      }

      private com.google.protobuf.ByteString f2_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes f2 = 2;</code>
       */
      public boolean hasF2() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional bytes f2 = 2;</code>
       */
      public com.google.protobuf.ByteString getF2() {
        return f2_;
      }
      /**
       * <code>optional bytes f2 = 2;</code>
       */
      public Builder setF2(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        f2_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes f2 = 2;</code>
       */
      public Builder clearF2() {
        bitField0_ = (bitField0_ & ~0x00000002);
        f2_ = getDefaultInstance().getF2();
        onChanged();
        return this;
      }

      private int f4_ ;
      /**
       * <code>optional uint32 f4 = 4;</code>
       */
      public boolean hasF4() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional uint32 f4 = 4;</code>
       */
      public int getF4() {
        return f4_;
      }
      /**
       * <code>optional uint32 f4 = 4;</code>
       */
      public Builder setF4(int value) {
        bitField0_ |= 0x00000004;
        f4_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 f4 = 4;</code>
       */
      public Builder clearF4() {
        bitField0_ = (bitField0_ & ~0x00000004);
        f4_ = 0;
        onChanged();
        return this;
      }

      private int f5_ ;
      /**
       * <code>optional uint32 f5 = 5;</code>
       */
      public boolean hasF5() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>optional uint32 f5 = 5;</code>
       */
      public int getF5() {
        return f5_;
      }
      /**
       * <code>optional uint32 f5 = 5;</code>
       */
      public Builder setF5(int value) {
        bitField0_ |= 0x00000008;
        f5_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 f5 = 5;</code>
       */
      public Builder clearF5() {
        bitField0_ = (bitField0_ & ~0x00000008);
        f5_ = 0;
        onChanged();
        return this;
      }

      private int f6_ ;
      /**
       * <code>optional uint32 f6 = 6;</code>
       */
      public boolean hasF6() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      /**
       * <code>optional uint32 f6 = 6;</code>
       */
      public int getF6() {
        return f6_;
      }
      /**
       * <code>optional uint32 f6 = 6;</code>
       */
      public Builder setF6(int value) {
        bitField0_ |= 0x00000010;
        f6_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 f6 = 6;</code>
       */
      public Builder clearF6() {
        bitField0_ = (bitField0_ & ~0x00000010);
        f6_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:M201ResponseBody)
    }

    static {
      defaultInstance = new M201ResponseBody(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:M201ResponseBody)
  }

  public interface M211RequestOrBuilder extends
      // @@protoc_insertion_point(interface_extends:M211Request)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional .ItemOp itemOp = 1;</code>
     */
    boolean hasItemOp();
    /**
     * <code>optional .ItemOp itemOp = 1;</code>
     */
    CK.ItemOp getItemOp();
    /**
     * <code>optional .ItemOp itemOp = 1;</code>
     */
    CK.ItemOpOrBuilder getItemOpOrBuilder();

    /**
     * <code>optional .UInt32 f6 = 6;</code>
     */
    boolean hasF6();
    /**
     * <code>optional .UInt32 f6 = 6;</code>
     */
    CK.UInt32 getF6();
    /**
     * <code>optional .UInt32 f6 = 6;</code>
     */
    CK.UInt32OrBuilder getF6OrBuilder();
  }
  /**
   * Protobuf type {@code M211Request}
   */
  public static final class M211Request extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:M211Request)
      M211RequestOrBuilder {
    // Use M211Request.newBuilder() to construct.
    private M211Request(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private M211Request(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final M211Request defaultInstance;
    public static M211Request getDefaultInstance() {
      return defaultInstance;
    }

    public M211Request getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private M211Request(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
              CK.ItemOp.Builder subBuilder = null;
              if (((bitField0_ & 0x00000001) == 0x00000001)) {
                subBuilder = itemOp_.toBuilder();
              }
              itemOp_ = input.readMessage(CK.ItemOp.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(itemOp_);
                itemOp_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000001;
              break;
            }
            case 50: {
              CK.UInt32.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = f6_.toBuilder();
              }
              f6_ = input.readMessage(CK.UInt32.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(f6_);
                f6_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_M211Request_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_M211Request_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.M211Request.class, CK.M211Request.Builder.class);
    }

    public static com.google.protobuf.Parser<M211Request> PARSER =
        new com.google.protobuf.AbstractParser<M211Request>() {
      public M211Request parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new M211Request(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<M211Request> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int ITEMOP_FIELD_NUMBER = 1;
    private CK.ItemOp itemOp_;
    /**
     * <code>optional .ItemOp itemOp = 1;</code>
     */
    public boolean hasItemOp() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional .ItemOp itemOp = 1;</code>
     */
    public CK.ItemOp getItemOp() {
      return itemOp_;
    }
    /**
     * <code>optional .ItemOp itemOp = 1;</code>
     */
    public CK.ItemOpOrBuilder getItemOpOrBuilder() {
      return itemOp_;
    }

    public static final int F6_FIELD_NUMBER = 6;
    private CK.UInt32 f6_;
    /**
     * <code>optional .UInt32 f6 = 6;</code>
     */
    public boolean hasF6() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional .UInt32 f6 = 6;</code>
     */
    public CK.UInt32 getF6() {
      return f6_;
    }
    /**
     * <code>optional .UInt32 f6 = 6;</code>
     */
    public CK.UInt32OrBuilder getF6OrBuilder() {
      return f6_;
    }

    private void initFields() {
      itemOp_ = CK.ItemOp.getDefaultInstance();
      f6_ = CK.UInt32.getDefaultInstance();
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeMessage(1, itemOp_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(6, f6_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, itemOp_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(6, f6_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.M211Request parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.M211Request parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.M211Request parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.M211Request parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.M211Request parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.M211Request parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.M211Request parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.M211Request parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.M211Request parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.M211Request parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.M211Request prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code M211Request}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:M211Request)
        CK.M211RequestOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_M211Request_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_M211Request_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.M211Request.class, CK.M211Request.Builder.class);
      }

      // Construct using CloudKit.M211Request.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getItemOpFieldBuilder();
          getF6FieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (itemOpBuilder_ == null) {
          itemOp_ = CK.ItemOp.getDefaultInstance();
        } else {
          itemOpBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        if (f6Builder_ == null) {
          f6_ = CK.UInt32.getDefaultInstance();
        } else {
          f6Builder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_M211Request_descriptor;
      }

      public CK.M211Request getDefaultInstanceForType() {
        return CK.M211Request.getDefaultInstance();
      }

      public CK.M211Request build() {
        CK.M211Request result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.M211Request buildPartial() {
        CK.M211Request result = new CK.M211Request(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (itemOpBuilder_ == null) {
          result.itemOp_ = itemOp_;
        } else {
          result.itemOp_ = itemOpBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (f6Builder_ == null) {
          result.f6_ = f6_;
        } else {
          result.f6_ = f6Builder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.M211Request) {
          return mergeFrom((CK.M211Request)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.M211Request other) {
        if (other == CK.M211Request.getDefaultInstance()) return this;
        if (other.hasItemOp()) {
          mergeItemOp(other.getItemOp());
        }
        if (other.hasF6()) {
          mergeF6(other.getF6());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.M211Request parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.M211Request) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private CK.ItemOp itemOp_ = CK.ItemOp.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.ItemOp, CK.ItemOp.Builder, CK.ItemOpOrBuilder> itemOpBuilder_;
      /**
       * <code>optional .ItemOp itemOp = 1;</code>
       */
      public boolean hasItemOp() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional .ItemOp itemOp = 1;</code>
       */
      public CK.ItemOp getItemOp() {
        if (itemOpBuilder_ == null) {
          return itemOp_;
        } else {
          return itemOpBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .ItemOp itemOp = 1;</code>
       */
      public Builder setItemOp(CK.ItemOp value) {
        if (itemOpBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          itemOp_ = value;
          onChanged();
        } else {
          itemOpBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .ItemOp itemOp = 1;</code>
       */
      public Builder setItemOp(
          CK.ItemOp.Builder builderForValue) {
        if (itemOpBuilder_ == null) {
          itemOp_ = builderForValue.build();
          onChanged();
        } else {
          itemOpBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .ItemOp itemOp = 1;</code>
       */
      public Builder mergeItemOp(CK.ItemOp value) {
        if (itemOpBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              itemOp_ != CK.ItemOp.getDefaultInstance()) {
            itemOp_ =
              CK.ItemOp.newBuilder(itemOp_).mergeFrom(value).buildPartial();
          } else {
            itemOp_ = value;
          }
          onChanged();
        } else {
          itemOpBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .ItemOp itemOp = 1;</code>
       */
      public Builder clearItemOp() {
        if (itemOpBuilder_ == null) {
          itemOp_ = CK.ItemOp.getDefaultInstance();
          onChanged();
        } else {
          itemOpBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      /**
       * <code>optional .ItemOp itemOp = 1;</code>
       */
      public CK.ItemOp.Builder getItemOpBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getItemOpFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .ItemOp itemOp = 1;</code>
       */
      public CK.ItemOpOrBuilder getItemOpOrBuilder() {
        if (itemOpBuilder_ != null) {
          return itemOpBuilder_.getMessageOrBuilder();
        } else {
          return itemOp_;
        }
      }
      /**
       * <code>optional .ItemOp itemOp = 1;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.ItemOp, CK.ItemOp.Builder, CK.ItemOpOrBuilder> 
          getItemOpFieldBuilder() {
        if (itemOpBuilder_ == null) {
          itemOpBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.ItemOp, CK.ItemOp.Builder, CK.ItemOpOrBuilder>(
                  getItemOp(),
                  getParentForChildren(),
                  isClean());
          itemOp_ = null;
        }
        return itemOpBuilder_;
      }

      private CK.UInt32 f6_ = CK.UInt32.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.UInt32, CK.UInt32.Builder, CK.UInt32OrBuilder> f6Builder_;
      /**
       * <code>optional .UInt32 f6 = 6;</code>
       */
      public boolean hasF6() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional .UInt32 f6 = 6;</code>
       */
      public CK.UInt32 getF6() {
        if (f6Builder_ == null) {
          return f6_;
        } else {
          return f6Builder_.getMessage();
        }
      }
      /**
       * <code>optional .UInt32 f6 = 6;</code>
       */
      public Builder setF6(CK.UInt32 value) {
        if (f6Builder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          f6_ = value;
          onChanged();
        } else {
          f6Builder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .UInt32 f6 = 6;</code>
       */
      public Builder setF6(
          CK.UInt32.Builder builderForValue) {
        if (f6Builder_ == null) {
          f6_ = builderForValue.build();
          onChanged();
        } else {
          f6Builder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .UInt32 f6 = 6;</code>
       */
      public Builder mergeF6(CK.UInt32 value) {
        if (f6Builder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              f6_ != CK.UInt32.getDefaultInstance()) {
            f6_ =
              CK.UInt32.newBuilder(f6_).mergeFrom(value).buildPartial();
          } else {
            f6_ = value;
          }
          onChanged();
        } else {
          f6Builder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .UInt32 f6 = 6;</code>
       */
      public Builder clearF6() {
        if (f6Builder_ == null) {
          f6_ = CK.UInt32.getDefaultInstance();
          onChanged();
        } else {
          f6Builder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      /**
       * <code>optional .UInt32 f6 = 6;</code>
       */
      public CK.UInt32.Builder getF6Builder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getF6FieldBuilder().getBuilder();
      }
      /**
       * <code>optional .UInt32 f6 = 6;</code>
       */
      public CK.UInt32OrBuilder getF6OrBuilder() {
        if (f6Builder_ != null) {
          return f6Builder_.getMessageOrBuilder();
        } else {
          return f6_;
        }
      }
      /**
       * <code>optional .UInt32 f6 = 6;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.UInt32, CK.UInt32.Builder, CK.UInt32OrBuilder> 
          getF6FieldBuilder() {
        if (f6Builder_ == null) {
          f6Builder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.UInt32, CK.UInt32.Builder, CK.UInt32OrBuilder>(
                  getF6(),
                  getParentForChildren(),
                  isClean());
          f6_ = null;
        }
        return f6Builder_;
      }

      // @@protoc_insertion_point(builder_scope:M211Request)
    }

    static {
      defaultInstance = new M211Request(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:M211Request)
  }

  public interface M211ResponseOrBuilder extends
      // @@protoc_insertion_point(interface_extends:M211Response)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional .M211ResponseBody body = 1;</code>
     */
    boolean hasBody();
    /**
     * <code>optional .M211ResponseBody body = 1;</code>
     */
    CK.M211ResponseBody getBody();
    /**
     * <code>optional .M211ResponseBody body = 1;</code>
     */
    CK.M211ResponseBodyOrBuilder getBodyOrBuilder();

    /**
     * <code>optional uint32 f2 = 2;</code>
     */
    boolean hasF2();
    /**
     * <code>optional uint32 f2 = 2;</code>
     */
    int getF2();
  }
  /**
   * Protobuf type {@code M211Response}
   *
   * <pre>
   * Record_Response_211
   * </pre>
   */
  public static final class M211Response extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:M211Response)
      M211ResponseOrBuilder {
    // Use M211Response.newBuilder() to construct.
    private M211Response(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private M211Response(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final M211Response defaultInstance;
    public static M211Response getDefaultInstance() {
      return defaultInstance;
    }

    public M211Response getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private M211Response(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
              CK.M211ResponseBody.Builder subBuilder = null;
              if (((bitField0_ & 0x00000001) == 0x00000001)) {
                subBuilder = body_.toBuilder();
              }
              body_ = input.readMessage(CK.M211ResponseBody.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(body_);
                body_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000001;
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              f2_ = input.readUInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_M211Response_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_M211Response_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.M211Response.class, CK.M211Response.Builder.class);
    }

    public static com.google.protobuf.Parser<M211Response> PARSER =
        new com.google.protobuf.AbstractParser<M211Response>() {
      public M211Response parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new M211Response(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<M211Response> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int BODY_FIELD_NUMBER = 1;
    private CK.M211ResponseBody body_;
    /**
     * <code>optional .M211ResponseBody body = 1;</code>
     */
    public boolean hasBody() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional .M211ResponseBody body = 1;</code>
     */
    public CK.M211ResponseBody getBody() {
      return body_;
    }
    /**
     * <code>optional .M211ResponseBody body = 1;</code>
     */
    public CK.M211ResponseBodyOrBuilder getBodyOrBuilder() {
      return body_;
    }

    public static final int F2_FIELD_NUMBER = 2;
    private int f2_;
    /**
     * <code>optional uint32 f2 = 2;</code>
     */
    public boolean hasF2() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional uint32 f2 = 2;</code>
     */
    public int getF2() {
      return f2_;
    }

    private void initFields() {
      body_ = CK.M211ResponseBody.getDefaultInstance();
      f2_ = 0;
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeMessage(1, body_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeUInt32(2, f2_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, body_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(2, f2_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.M211Response parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.M211Response parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.M211Response parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.M211Response parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.M211Response parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.M211Response parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.M211Response parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.M211Response parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.M211Response parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.M211Response parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.M211Response prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code M211Response}
     *
     * <pre>
     * Record_Response_211
     * </pre>
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:M211Response)
        CK.M211ResponseOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_M211Response_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_M211Response_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.M211Response.class, CK.M211Response.Builder.class);
      }

      // Construct using CloudKit.M211Response.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getBodyFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (bodyBuilder_ == null) {
          body_ = CK.M211ResponseBody.getDefaultInstance();
        } else {
          bodyBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        f2_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_M211Response_descriptor;
      }

      public CK.M211Response getDefaultInstanceForType() {
        return CK.M211Response.getDefaultInstance();
      }

      public CK.M211Response build() {
        CK.M211Response result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.M211Response buildPartial() {
        CK.M211Response result = new CK.M211Response(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (bodyBuilder_ == null) {
          result.body_ = body_;
        } else {
          result.body_ = bodyBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.f2_ = f2_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.M211Response) {
          return mergeFrom((CK.M211Response)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.M211Response other) {
        if (other == CK.M211Response.getDefaultInstance()) return this;
        if (other.hasBody()) {
          mergeBody(other.getBody());
        }
        if (other.hasF2()) {
          setF2(other.getF2());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.M211Response parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.M211Response) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private CK.M211ResponseBody body_ = CK.M211ResponseBody.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.M211ResponseBody, CK.M211ResponseBody.Builder, CK.M211ResponseBodyOrBuilder> bodyBuilder_;
      /**
       * <code>optional .M211ResponseBody body = 1;</code>
       */
      public boolean hasBody() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional .M211ResponseBody body = 1;</code>
       */
      public CK.M211ResponseBody getBody() {
        if (bodyBuilder_ == null) {
          return body_;
        } else {
          return bodyBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .M211ResponseBody body = 1;</code>
       */
      public Builder setBody(CK.M211ResponseBody value) {
        if (bodyBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          body_ = value;
          onChanged();
        } else {
          bodyBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .M211ResponseBody body = 1;</code>
       */
      public Builder setBody(
          CK.M211ResponseBody.Builder builderForValue) {
        if (bodyBuilder_ == null) {
          body_ = builderForValue.build();
          onChanged();
        } else {
          bodyBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .M211ResponseBody body = 1;</code>
       */
      public Builder mergeBody(CK.M211ResponseBody value) {
        if (bodyBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              body_ != CK.M211ResponseBody.getDefaultInstance()) {
            body_ =
              CK.M211ResponseBody.newBuilder(body_).mergeFrom(value).buildPartial();
          } else {
            body_ = value;
          }
          onChanged();
        } else {
          bodyBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .M211ResponseBody body = 1;</code>
       */
      public Builder clearBody() {
        if (bodyBuilder_ == null) {
          body_ = CK.M211ResponseBody.getDefaultInstance();
          onChanged();
        } else {
          bodyBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      /**
       * <code>optional .M211ResponseBody body = 1;</code>
       */
      public CK.M211ResponseBody.Builder getBodyBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getBodyFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .M211ResponseBody body = 1;</code>
       */
      public CK.M211ResponseBodyOrBuilder getBodyOrBuilder() {
        if (bodyBuilder_ != null) {
          return bodyBuilder_.getMessageOrBuilder();
        } else {
          return body_;
        }
      }
      /**
       * <code>optional .M211ResponseBody body = 1;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.M211ResponseBody, CK.M211ResponseBody.Builder, CK.M211ResponseBodyOrBuilder> 
          getBodyFieldBuilder() {
        if (bodyBuilder_ == null) {
          bodyBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.M211ResponseBody, CK.M211ResponseBody.Builder, CK.M211ResponseBodyOrBuilder>(
                  getBody(),
                  getParentForChildren(),
                  isClean());
          body_ = null;
        }
        return bodyBuilder_;
      }

      private int f2_ ;
      /**
       * <code>optional uint32 f2 = 2;</code>
       */
      public boolean hasF2() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional uint32 f2 = 2;</code>
       */
      public int getF2() {
        return f2_;
      }
      /**
       * <code>optional uint32 f2 = 2;</code>
       */
      public Builder setF2(int value) {
        bitField0_ |= 0x00000002;
        f2_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 f2 = 2;</code>
       */
      public Builder clearF2() {
        bitField0_ = (bitField0_ & ~0x00000002);
        f2_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:M211Response)
    }

    static {
      defaultInstance = new M211Response(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:M211Response)
  }

  public interface M211ResponseBodyOrBuilder extends
      // @@protoc_insertion_point(interface_extends:M211ResponseBody)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string f1 = 1;</code>
     */
    boolean hasF1();
    /**
     * <code>optional string f1 = 1;</code>
     */
    java.lang.String getF1();
    /**
     * <code>optional string f1 = 1;</code>
     */
    com.google.protobuf.ByteString
        getF1Bytes();

    /**
     * <code>optional .ItemOp itemOp = 2;</code>
     */
    boolean hasItemOp();
    /**
     * <code>optional .ItemOp itemOp = 2;</code>
     */
    CK.ItemOp getItemOp();
    /**
     * <code>optional .ItemOp itemOp = 2;</code>
     */
    CK.ItemOpOrBuilder getItemOpOrBuilder();

    /**
     * <code>optional .String record = 3;</code>
     */
    boolean hasRecord();
    /**
     * <code>optional .String record = 3;</code>
     */
    CK.String getRecord();
    /**
     * <code>optional .String record = 3;</code>
     */
    CK.StringOrBuilder getRecordOrBuilder();

    /**
     * <code>optional .Item userID = 4;</code>
     */
    boolean hasUserID();
    /**
     * <code>optional .Item userID = 4;</code>
     */
    CK.Item getUserID();
    /**
     * <code>optional .Item userID = 4;</code>
     */
    CK.ItemOrBuilder getUserIDOrBuilder();

    /**
     * <code>optional .Fixed64Pair f5 = 5;</code>
     */
    boolean hasF5();
    /**
     * <code>optional .Fixed64Pair f5 = 5;</code>
     */
    CK.Fixed64Pair getF5();
    /**
     * <code>optional .Fixed64Pair f5 = 5;</code>
     */
    CK.Fixed64PairOrBuilder getF5OrBuilder();

    /**
     * <code>repeated .Container container = 7;</code>
     */
    java.util.List<CK.Container> 
        getContainerList();
    /**
     * <code>repeated .Container container = 7;</code>
     */
    CK.Container getContainer(int index);
    /**
     * <code>repeated .Container container = 7;</code>
     */
    int getContainerCount();
    /**
     * <code>repeated .Container container = 7;</code>
     */
    java.util.List<? extends CK.ContainerOrBuilder> 
        getContainerOrBuilderList();
    /**
     * <code>repeated .Container container = 7;</code>
     */
    CK.ContainerOrBuilder getContainerOrBuilder(
        int index);

    /**
     * <code>optional .Item ckUserID = 9;</code>
     */
    boolean hasCkUserID();
    /**
     * <code>optional .Item ckUserID = 9;</code>
     */
    CK.Item getCkUserID();
    /**
     * <code>optional .Item ckUserID = 9;</code>
     */
    CK.ItemOrBuilder getCkUserIDOrBuilder();

    /**
     * <code>optional string deviceName = 11;</code>
     */
    boolean hasDeviceName();
    /**
     * <code>optional string deviceName = 11;</code>
     */
    java.lang.String getDeviceName();
    /**
     * <code>optional string deviceName = 11;</code>
     */
    com.google.protobuf.ByteString
        getDeviceNameBytes();

    /**
     * <code>optional .BytesString f13 = 13;</code>
     */
    boolean hasF13();
    /**
     * <code>optional .BytesString f13 = 13;</code>
     */
    CK.BytesString getF13();
    /**
     * <code>optional .BytesString f13 = 13;</code>
     */
    CK.BytesStringOrBuilder getF13OrBuilder();

    /**
     * <code>optional uint32 f15 = 15;</code>
     */
    boolean hasF15();
    /**
     * <code>optional uint32 f15 = 15;</code>
     */
    int getF15();
  }
  /**
   * Protobuf type {@code M211ResponseBody}
   *
   * <pre>
   * Record_Response_211_1
   * </pre>
   */
  public static final class M211ResponseBody extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:M211ResponseBody)
      M211ResponseBodyOrBuilder {
    // Use M211ResponseBody.newBuilder() to construct.
    private M211ResponseBody(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private M211ResponseBody(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final M211ResponseBody defaultInstance;
    public static M211ResponseBody getDefaultInstance() {
      return defaultInstance;
    }

    public M211ResponseBody getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private M211ResponseBody(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
              f1_ = bs;
              break;
            }
            case 18: {
              CK.ItemOp.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = itemOp_.toBuilder();
              }
              itemOp_ = input.readMessage(CK.ItemOp.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(itemOp_);
                itemOp_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
            case 26: {
              CK.String.Builder subBuilder = null;
              if (((bitField0_ & 0x00000004) == 0x00000004)) {
                subBuilder = record_.toBuilder();
              }
              record_ = input.readMessage(CK.String.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(record_);
                record_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000004;
              break;
            }
            case 34: {
              CK.Item.Builder subBuilder = null;
              if (((bitField0_ & 0x00000008) == 0x00000008)) {
                subBuilder = userID_.toBuilder();
              }
              userID_ = input.readMessage(CK.Item.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(userID_);
                userID_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000008;
              break;
            }
            case 42: {
              CK.Fixed64Pair.Builder subBuilder = null;
              if (((bitField0_ & 0x00000010) == 0x00000010)) {
                subBuilder = f5_.toBuilder();
              }
              f5_ = input.readMessage(CK.Fixed64Pair.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(f5_);
                f5_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000010;
              break;
            }
            case 58: {
              if (!((mutable_bitField0_ & 0x00000020) == 0x00000020)) {
                container_ = new java.util.ArrayList<CK.Container>();
                mutable_bitField0_ |= 0x00000020;
              }
              container_.add(input.readMessage(CK.Container.PARSER, extensionRegistry));
              break;
            }
            case 74: {
              CK.Item.Builder subBuilder = null;
              if (((bitField0_ & 0x00000020) == 0x00000020)) {
                subBuilder = ckUserID_.toBuilder();
              }
              ckUserID_ = input.readMessage(CK.Item.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(ckUserID_);
                ckUserID_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000020;
              break;
            }
            case 90: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000040;
              deviceName_ = bs;
              break;
            }
            case 106: {
              CK.BytesString.Builder subBuilder = null;
              if (((bitField0_ & 0x00000080) == 0x00000080)) {
                subBuilder = f13_.toBuilder();
              }
              f13_ = input.readMessage(CK.BytesString.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(f13_);
                f13_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000080;
              break;
            }
            case 120: {
              bitField0_ |= 0x00000100;
              f15_ = input.readUInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000020) == 0x00000020)) {
          container_ = java.util.Collections.unmodifiableList(container_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_M211ResponseBody_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_M211ResponseBody_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.M211ResponseBody.class, CK.M211ResponseBody.Builder.class);
    }

    public static com.google.protobuf.Parser<M211ResponseBody> PARSER =
        new com.google.protobuf.AbstractParser<M211ResponseBody>() {
      public M211ResponseBody parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new M211ResponseBody(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<M211ResponseBody> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int F1_FIELD_NUMBER = 1;
    private java.lang.Object f1_;
    /**
     * <code>optional string f1 = 1;</code>
     */
    public boolean hasF1() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional string f1 = 1;</code>
     */
    public java.lang.String getF1() {
      java.lang.Object ref = f1_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          f1_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string f1 = 1;</code>
     */
    public com.google.protobuf.ByteString
        getF1Bytes() {
      java.lang.Object ref = f1_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        f1_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int ITEMOP_FIELD_NUMBER = 2;
    private CK.ItemOp itemOp_;
    /**
     * <code>optional .ItemOp itemOp = 2;</code>
     */
    public boolean hasItemOp() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional .ItemOp itemOp = 2;</code>
     */
    public CK.ItemOp getItemOp() {
      return itemOp_;
    }
    /**
     * <code>optional .ItemOp itemOp = 2;</code>
     */
    public CK.ItemOpOrBuilder getItemOpOrBuilder() {
      return itemOp_;
    }

    public static final int RECORD_FIELD_NUMBER = 3;
    private CK.String record_;
    /**
     * <code>optional .String record = 3;</code>
     */
    public boolean hasRecord() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional .String record = 3;</code>
     */
    public CK.String getRecord() {
      return record_;
    }
    /**
     * <code>optional .String record = 3;</code>
     */
    public CK.StringOrBuilder getRecordOrBuilder() {
      return record_;
    }

    public static final int USERID_FIELD_NUMBER = 4;
    private CK.Item userID_;
    /**
     * <code>optional .Item userID = 4;</code>
     */
    public boolean hasUserID() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional .Item userID = 4;</code>
     */
    public CK.Item getUserID() {
      return userID_;
    }
    /**
     * <code>optional .Item userID = 4;</code>
     */
    public CK.ItemOrBuilder getUserIDOrBuilder() {
      return userID_;
    }

    public static final int F5_FIELD_NUMBER = 5;
    private CK.Fixed64Pair f5_;
    /**
     * <code>optional .Fixed64Pair f5 = 5;</code>
     */
    public boolean hasF5() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    /**
     * <code>optional .Fixed64Pair f5 = 5;</code>
     */
    public CK.Fixed64Pair getF5() {
      return f5_;
    }
    /**
     * <code>optional .Fixed64Pair f5 = 5;</code>
     */
    public CK.Fixed64PairOrBuilder getF5OrBuilder() {
      return f5_;
    }

    public static final int CONTAINER_FIELD_NUMBER = 7;
    private java.util.List<CK.Container> container_;
    /**
     * <code>repeated .Container container = 7;</code>
     */
    public java.util.List<CK.Container> getContainerList() {
      return container_;
    }
    /**
     * <code>repeated .Container container = 7;</code>
     */
    public java.util.List<? extends CK.ContainerOrBuilder> 
        getContainerOrBuilderList() {
      return container_;
    }
    /**
     * <code>repeated .Container container = 7;</code>
     */
    public int getContainerCount() {
      return container_.size();
    }
    /**
     * <code>repeated .Container container = 7;</code>
     */
    public CK.Container getContainer(int index) {
      return container_.get(index);
    }
    /**
     * <code>repeated .Container container = 7;</code>
     */
    public CK.ContainerOrBuilder getContainerOrBuilder(
        int index) {
      return container_.get(index);
    }

    public static final int CKUSERID_FIELD_NUMBER = 9;
    private CK.Item ckUserID_;
    /**
     * <code>optional .Item ckUserID = 9;</code>
     */
    public boolean hasCkUserID() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    /**
     * <code>optional .Item ckUserID = 9;</code>
     */
    public CK.Item getCkUserID() {
      return ckUserID_;
    }
    /**
     * <code>optional .Item ckUserID = 9;</code>
     */
    public CK.ItemOrBuilder getCkUserIDOrBuilder() {
      return ckUserID_;
    }

    public static final int DEVICENAME_FIELD_NUMBER = 11;
    private java.lang.Object deviceName_;
    /**
     * <code>optional string deviceName = 11;</code>
     */
    public boolean hasDeviceName() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }
    /**
     * <code>optional string deviceName = 11;</code>
     */
    public java.lang.String getDeviceName() {
      java.lang.Object ref = deviceName_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          deviceName_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string deviceName = 11;</code>
     */
    public com.google.protobuf.ByteString
        getDeviceNameBytes() {
      java.lang.Object ref = deviceName_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        deviceName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int F13_FIELD_NUMBER = 13;
    private CK.BytesString f13_;
    /**
     * <code>optional .BytesString f13 = 13;</code>
     */
    public boolean hasF13() {
      return ((bitField0_ & 0x00000080) == 0x00000080);
    }
    /**
     * <code>optional .BytesString f13 = 13;</code>
     */
    public CK.BytesString getF13() {
      return f13_;
    }
    /**
     * <code>optional .BytesString f13 = 13;</code>
     */
    public CK.BytesStringOrBuilder getF13OrBuilder() {
      return f13_;
    }

    public static final int F15_FIELD_NUMBER = 15;
    private int f15_;
    /**
     * <code>optional uint32 f15 = 15;</code>
     */
    public boolean hasF15() {
      return ((bitField0_ & 0x00000100) == 0x00000100);
    }
    /**
     * <code>optional uint32 f15 = 15;</code>
     */
    public int getF15() {
      return f15_;
    }

    private void initFields() {
      f1_ = "";
      itemOp_ = CK.ItemOp.getDefaultInstance();
      record_ = CK.String.getDefaultInstance();
      userID_ = CK.Item.getDefaultInstance();
      f5_ = CK.Fixed64Pair.getDefaultInstance();
      container_ = java.util.Collections.emptyList();
      ckUserID_ = CK.Item.getDefaultInstance();
      deviceName_ = "";
      f13_ = CK.BytesString.getDefaultInstance();
      f15_ = 0;
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getF1Bytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(2, itemOp_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeMessage(3, record_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeMessage(4, userID_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeMessage(5, f5_);
      }
      for (int i = 0; i < container_.size(); i++) {
        output.writeMessage(7, container_.get(i));
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeMessage(9, ckUserID_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeBytes(11, getDeviceNameBytes());
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        output.writeMessage(13, f13_);
      }
      if (((bitField0_ & 0x00000100) == 0x00000100)) {
        output.writeUInt32(15, f15_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getF1Bytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, itemOp_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, record_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, userID_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(5, f5_);
      }
      for (int i = 0; i < container_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(7, container_.get(i));
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(9, ckUserID_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(11, getDeviceNameBytes());
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(13, f13_);
      }
      if (((bitField0_ & 0x00000100) == 0x00000100)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(15, f15_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.M211ResponseBody parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.M211ResponseBody parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.M211ResponseBody parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.M211ResponseBody parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.M211ResponseBody parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.M211ResponseBody parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.M211ResponseBody parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.M211ResponseBody parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.M211ResponseBody parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.M211ResponseBody parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.M211ResponseBody prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code M211ResponseBody}
     *
     * <pre>
     * Record_Response_211_1
     * </pre>
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:M211ResponseBody)
        CK.M211ResponseBodyOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_M211ResponseBody_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_M211ResponseBody_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.M211ResponseBody.class, CK.M211ResponseBody.Builder.class);
      }

      // Construct using CloudKit.M211ResponseBody.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getItemOpFieldBuilder();
          getRecordFieldBuilder();
          getUserIDFieldBuilder();
          getF5FieldBuilder();
          getContainerFieldBuilder();
          getCkUserIDFieldBuilder();
          getF13FieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        f1_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        if (itemOpBuilder_ == null) {
          itemOp_ = CK.ItemOp.getDefaultInstance();
        } else {
          itemOpBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        if (recordBuilder_ == null) {
          record_ = CK.String.getDefaultInstance();
        } else {
          recordBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        if (userIDBuilder_ == null) {
          userID_ = CK.Item.getDefaultInstance();
        } else {
          userIDBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        if (f5Builder_ == null) {
          f5_ = CK.Fixed64Pair.getDefaultInstance();
        } else {
          f5Builder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        if (containerBuilder_ == null) {
          container_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000020);
        } else {
          containerBuilder_.clear();
        }
        if (ckUserIDBuilder_ == null) {
          ckUserID_ = CK.Item.getDefaultInstance();
        } else {
          ckUserIDBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000040);
        deviceName_ = "";
        bitField0_ = (bitField0_ & ~0x00000080);
        if (f13Builder_ == null) {
          f13_ = CK.BytesString.getDefaultInstance();
        } else {
          f13Builder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000100);
        f15_ = 0;
        bitField0_ = (bitField0_ & ~0x00000200);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_M211ResponseBody_descriptor;
      }

      public CK.M211ResponseBody getDefaultInstanceForType() {
        return CK.M211ResponseBody.getDefaultInstance();
      }

      public CK.M211ResponseBody build() {
        CK.M211ResponseBody result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.M211ResponseBody buildPartial() {
        CK.M211ResponseBody result = new CK.M211ResponseBody(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.f1_ = f1_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (itemOpBuilder_ == null) {
          result.itemOp_ = itemOp_;
        } else {
          result.itemOp_ = itemOpBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        if (recordBuilder_ == null) {
          result.record_ = record_;
        } else {
          result.record_ = recordBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        if (userIDBuilder_ == null) {
          result.userID_ = userID_;
        } else {
          result.userID_ = userIDBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        if (f5Builder_ == null) {
          result.f5_ = f5_;
        } else {
          result.f5_ = f5Builder_.build();
        }
        if (containerBuilder_ == null) {
          if (((bitField0_ & 0x00000020) == 0x00000020)) {
            container_ = java.util.Collections.unmodifiableList(container_);
            bitField0_ = (bitField0_ & ~0x00000020);
          }
          result.container_ = container_;
        } else {
          result.container_ = containerBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000020;
        }
        if (ckUserIDBuilder_ == null) {
          result.ckUserID_ = ckUserID_;
        } else {
          result.ckUserID_ = ckUserIDBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000080) == 0x00000080)) {
          to_bitField0_ |= 0x00000040;
        }
        result.deviceName_ = deviceName_;
        if (((from_bitField0_ & 0x00000100) == 0x00000100)) {
          to_bitField0_ |= 0x00000080;
        }
        if (f13Builder_ == null) {
          result.f13_ = f13_;
        } else {
          result.f13_ = f13Builder_.build();
        }
        if (((from_bitField0_ & 0x00000200) == 0x00000200)) {
          to_bitField0_ |= 0x00000100;
        }
        result.f15_ = f15_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.M211ResponseBody) {
          return mergeFrom((CK.M211ResponseBody)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.M211ResponseBody other) {
        if (other == CK.M211ResponseBody.getDefaultInstance()) return this;
        if (other.hasF1()) {
          bitField0_ |= 0x00000001;
          f1_ = other.f1_;
          onChanged();
        }
        if (other.hasItemOp()) {
          mergeItemOp(other.getItemOp());
        }
        if (other.hasRecord()) {
          mergeRecord(other.getRecord());
        }
        if (other.hasUserID()) {
          mergeUserID(other.getUserID());
        }
        if (other.hasF5()) {
          mergeF5(other.getF5());
        }
        if (containerBuilder_ == null) {
          if (!other.container_.isEmpty()) {
            if (container_.isEmpty()) {
              container_ = other.container_;
              bitField0_ = (bitField0_ & ~0x00000020);
            } else {
              ensureContainerIsMutable();
              container_.addAll(other.container_);
            }
            onChanged();
          }
        } else {
          if (!other.container_.isEmpty()) {
            if (containerBuilder_.isEmpty()) {
              containerBuilder_.dispose();
              containerBuilder_ = null;
              container_ = other.container_;
              bitField0_ = (bitField0_ & ~0x00000020);
              containerBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getContainerFieldBuilder() : null;
            } else {
              containerBuilder_.addAllMessages(other.container_);
            }
          }
        }
        if (other.hasCkUserID()) {
          mergeCkUserID(other.getCkUserID());
        }
        if (other.hasDeviceName()) {
          bitField0_ |= 0x00000080;
          deviceName_ = other.deviceName_;
          onChanged();
        }
        if (other.hasF13()) {
          mergeF13(other.getF13());
        }
        if (other.hasF15()) {
          setF15(other.getF15());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.M211ResponseBody parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.M211ResponseBody) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object f1_ = "";
      /**
       * <code>optional string f1 = 1;</code>
       */
      public boolean hasF1() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional string f1 = 1;</code>
       */
      public java.lang.String getF1() {
        java.lang.Object ref = f1_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            f1_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string f1 = 1;</code>
       */
      public com.google.protobuf.ByteString
          getF1Bytes() {
        java.lang.Object ref = f1_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          f1_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string f1 = 1;</code>
       */
      public Builder setF1(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        f1_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string f1 = 1;</code>
       */
      public Builder clearF1() {
        bitField0_ = (bitField0_ & ~0x00000001);
        f1_ = getDefaultInstance().getF1();
        onChanged();
        return this;
      }
      /**
       * <code>optional string f1 = 1;</code>
       */
      public Builder setF1Bytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        f1_ = value;
        onChanged();
        return this;
      }

      private CK.ItemOp itemOp_ = CK.ItemOp.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.ItemOp, CK.ItemOp.Builder, CK.ItemOpOrBuilder> itemOpBuilder_;
      /**
       * <code>optional .ItemOp itemOp = 2;</code>
       */
      public boolean hasItemOp() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional .ItemOp itemOp = 2;</code>
       */
      public CK.ItemOp getItemOp() {
        if (itemOpBuilder_ == null) {
          return itemOp_;
        } else {
          return itemOpBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .ItemOp itemOp = 2;</code>
       */
      public Builder setItemOp(CK.ItemOp value) {
        if (itemOpBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          itemOp_ = value;
          onChanged();
        } else {
          itemOpBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .ItemOp itemOp = 2;</code>
       */
      public Builder setItemOp(
          CK.ItemOp.Builder builderForValue) {
        if (itemOpBuilder_ == null) {
          itemOp_ = builderForValue.build();
          onChanged();
        } else {
          itemOpBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .ItemOp itemOp = 2;</code>
       */
      public Builder mergeItemOp(CK.ItemOp value) {
        if (itemOpBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              itemOp_ != CK.ItemOp.getDefaultInstance()) {
            itemOp_ =
              CK.ItemOp.newBuilder(itemOp_).mergeFrom(value).buildPartial();
          } else {
            itemOp_ = value;
          }
          onChanged();
        } else {
          itemOpBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .ItemOp itemOp = 2;</code>
       */
      public Builder clearItemOp() {
        if (itemOpBuilder_ == null) {
          itemOp_ = CK.ItemOp.getDefaultInstance();
          onChanged();
        } else {
          itemOpBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      /**
       * <code>optional .ItemOp itemOp = 2;</code>
       */
      public CK.ItemOp.Builder getItemOpBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getItemOpFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .ItemOp itemOp = 2;</code>
       */
      public CK.ItemOpOrBuilder getItemOpOrBuilder() {
        if (itemOpBuilder_ != null) {
          return itemOpBuilder_.getMessageOrBuilder();
        } else {
          return itemOp_;
        }
      }
      /**
       * <code>optional .ItemOp itemOp = 2;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.ItemOp, CK.ItemOp.Builder, CK.ItemOpOrBuilder> 
          getItemOpFieldBuilder() {
        if (itemOpBuilder_ == null) {
          itemOpBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.ItemOp, CK.ItemOp.Builder, CK.ItemOpOrBuilder>(
                  getItemOp(),
                  getParentForChildren(),
                  isClean());
          itemOp_ = null;
        }
        return itemOpBuilder_;
      }

      private CK.String record_ = CK.String.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.String, CK.String.Builder, CK.StringOrBuilder> recordBuilder_;
      /**
       * <code>optional .String record = 3;</code>
       */
      public boolean hasRecord() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional .String record = 3;</code>
       */
      public CK.String getRecord() {
        if (recordBuilder_ == null) {
          return record_;
        } else {
          return recordBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .String record = 3;</code>
       */
      public Builder setRecord(CK.String value) {
        if (recordBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          record_ = value;
          onChanged();
        } else {
          recordBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      /**
       * <code>optional .String record = 3;</code>
       */
      public Builder setRecord(
          CK.String.Builder builderForValue) {
        if (recordBuilder_ == null) {
          record_ = builderForValue.build();
          onChanged();
        } else {
          recordBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      /**
       * <code>optional .String record = 3;</code>
       */
      public Builder mergeRecord(CK.String value) {
        if (recordBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004) &&
              record_ != CK.String.getDefaultInstance()) {
            record_ =
              CK.String.newBuilder(record_).mergeFrom(value).buildPartial();
          } else {
            record_ = value;
          }
          onChanged();
        } else {
          recordBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      /**
       * <code>optional .String record = 3;</code>
       */
      public Builder clearRecord() {
        if (recordBuilder_ == null) {
          record_ = CK.String.getDefaultInstance();
          onChanged();
        } else {
          recordBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      /**
       * <code>optional .String record = 3;</code>
       */
      public CK.String.Builder getRecordBuilder() {
        bitField0_ |= 0x00000004;
        onChanged();
        return getRecordFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .String record = 3;</code>
       */
      public CK.StringOrBuilder getRecordOrBuilder() {
        if (recordBuilder_ != null) {
          return recordBuilder_.getMessageOrBuilder();
        } else {
          return record_;
        }
      }
      /**
       * <code>optional .String record = 3;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.String, CK.String.Builder, CK.StringOrBuilder> 
          getRecordFieldBuilder() {
        if (recordBuilder_ == null) {
          recordBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.String, CK.String.Builder, CK.StringOrBuilder>(
                  getRecord(),
                  getParentForChildren(),
                  isClean());
          record_ = null;
        }
        return recordBuilder_;
      }

      private CK.Item userID_ = CK.Item.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Item, CK.Item.Builder, CK.ItemOrBuilder> userIDBuilder_;
      /**
       * <code>optional .Item userID = 4;</code>
       */
      public boolean hasUserID() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>optional .Item userID = 4;</code>
       */
      public CK.Item getUserID() {
        if (userIDBuilder_ == null) {
          return userID_;
        } else {
          return userIDBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .Item userID = 4;</code>
       */
      public Builder setUserID(CK.Item value) {
        if (userIDBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          userID_ = value;
          onChanged();
        } else {
          userIDBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .Item userID = 4;</code>
       */
      public Builder setUserID(
          CK.Item.Builder builderForValue) {
        if (userIDBuilder_ == null) {
          userID_ = builderForValue.build();
          onChanged();
        } else {
          userIDBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .Item userID = 4;</code>
       */
      public Builder mergeUserID(CK.Item value) {
        if (userIDBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008) &&
              userID_ != CK.Item.getDefaultInstance()) {
            userID_ =
              CK.Item.newBuilder(userID_).mergeFrom(value).buildPartial();
          } else {
            userID_ = value;
          }
          onChanged();
        } else {
          userIDBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .Item userID = 4;</code>
       */
      public Builder clearUserID() {
        if (userIDBuilder_ == null) {
          userID_ = CK.Item.getDefaultInstance();
          onChanged();
        } else {
          userIDBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      /**
       * <code>optional .Item userID = 4;</code>
       */
      public CK.Item.Builder getUserIDBuilder() {
        bitField0_ |= 0x00000008;
        onChanged();
        return getUserIDFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Item userID = 4;</code>
       */
      public CK.ItemOrBuilder getUserIDOrBuilder() {
        if (userIDBuilder_ != null) {
          return userIDBuilder_.getMessageOrBuilder();
        } else {
          return userID_;
        }
      }
      /**
       * <code>optional .Item userID = 4;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Item, CK.Item.Builder, CK.ItemOrBuilder> 
          getUserIDFieldBuilder() {
        if (userIDBuilder_ == null) {
          userIDBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Item, CK.Item.Builder, CK.ItemOrBuilder>(
                  getUserID(),
                  getParentForChildren(),
                  isClean());
          userID_ = null;
        }
        return userIDBuilder_;
      }

      private CK.Fixed64Pair f5_ = CK.Fixed64Pair.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Fixed64Pair, CK.Fixed64Pair.Builder, CK.Fixed64PairOrBuilder> f5Builder_;
      /**
       * <code>optional .Fixed64Pair f5 = 5;</code>
       */
      public boolean hasF5() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      /**
       * <code>optional .Fixed64Pair f5 = 5;</code>
       */
      public CK.Fixed64Pair getF5() {
        if (f5Builder_ == null) {
          return f5_;
        } else {
          return f5Builder_.getMessage();
        }
      }
      /**
       * <code>optional .Fixed64Pair f5 = 5;</code>
       */
      public Builder setF5(CK.Fixed64Pair value) {
        if (f5Builder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          f5_ = value;
          onChanged();
        } else {
          f5Builder_.setMessage(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      /**
       * <code>optional .Fixed64Pair f5 = 5;</code>
       */
      public Builder setF5(
          CK.Fixed64Pair.Builder builderForValue) {
        if (f5Builder_ == null) {
          f5_ = builderForValue.build();
          onChanged();
        } else {
          f5Builder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      /**
       * <code>optional .Fixed64Pair f5 = 5;</code>
       */
      public Builder mergeF5(CK.Fixed64Pair value) {
        if (f5Builder_ == null) {
          if (((bitField0_ & 0x00000010) == 0x00000010) &&
              f5_ != CK.Fixed64Pair.getDefaultInstance()) {
            f5_ =
              CK.Fixed64Pair.newBuilder(f5_).mergeFrom(value).buildPartial();
          } else {
            f5_ = value;
          }
          onChanged();
        } else {
          f5Builder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      /**
       * <code>optional .Fixed64Pair f5 = 5;</code>
       */
      public Builder clearF5() {
        if (f5Builder_ == null) {
          f5_ = CK.Fixed64Pair.getDefaultInstance();
          onChanged();
        } else {
          f5Builder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }
      /**
       * <code>optional .Fixed64Pair f5 = 5;</code>
       */
      public CK.Fixed64Pair.Builder getF5Builder() {
        bitField0_ |= 0x00000010;
        onChanged();
        return getF5FieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Fixed64Pair f5 = 5;</code>
       */
      public CK.Fixed64PairOrBuilder getF5OrBuilder() {
        if (f5Builder_ != null) {
          return f5Builder_.getMessageOrBuilder();
        } else {
          return f5_;
        }
      }
      /**
       * <code>optional .Fixed64Pair f5 = 5;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Fixed64Pair, CK.Fixed64Pair.Builder, CK.Fixed64PairOrBuilder> 
          getF5FieldBuilder() {
        if (f5Builder_ == null) {
          f5Builder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Fixed64Pair, CK.Fixed64Pair.Builder, CK.Fixed64PairOrBuilder>(
                  getF5(),
                  getParentForChildren(),
                  isClean());
          f5_ = null;
        }
        return f5Builder_;
      }

      private java.util.List<CK.Container> container_ =
        java.util.Collections.emptyList();
      private void ensureContainerIsMutable() {
        if (!((bitField0_ & 0x00000020) == 0x00000020)) {
          container_ = new java.util.ArrayList<CK.Container>(container_);
          bitField0_ |= 0x00000020;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          CK.Container, CK.Container.Builder, CK.ContainerOrBuilder> containerBuilder_;

      /**
       * <code>repeated .Container container = 7;</code>
       */
      public java.util.List<CK.Container> getContainerList() {
        if (containerBuilder_ == null) {
          return java.util.Collections.unmodifiableList(container_);
        } else {
          return containerBuilder_.getMessageList();
        }
      }
      /**
       * <code>repeated .Container container = 7;</code>
       */
      public int getContainerCount() {
        if (containerBuilder_ == null) {
          return container_.size();
        } else {
          return containerBuilder_.getCount();
        }
      }
      /**
       * <code>repeated .Container container = 7;</code>
       */
      public CK.Container getContainer(int index) {
        if (containerBuilder_ == null) {
          return container_.get(index);
        } else {
          return containerBuilder_.getMessage(index);
        }
      }
      /**
       * <code>repeated .Container container = 7;</code>
       */
      public Builder setContainer(
          int index, CK.Container value) {
        if (containerBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureContainerIsMutable();
          container_.set(index, value);
          onChanged();
        } else {
          containerBuilder_.setMessage(index, value);
        }
        return this;
      }
      /**
       * <code>repeated .Container container = 7;</code>
       */
      public Builder setContainer(
          int index, CK.Container.Builder builderForValue) {
        if (containerBuilder_ == null) {
          ensureContainerIsMutable();
          container_.set(index, builderForValue.build());
          onChanged();
        } else {
          containerBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .Container container = 7;</code>
       */
      public Builder addContainer(CK.Container value) {
        if (containerBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureContainerIsMutable();
          container_.add(value);
          onChanged();
        } else {
          containerBuilder_.addMessage(value);
        }
        return this;
      }
      /**
       * <code>repeated .Container container = 7;</code>
       */
      public Builder addContainer(
          int index, CK.Container value) {
        if (containerBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureContainerIsMutable();
          container_.add(index, value);
          onChanged();
        } else {
          containerBuilder_.addMessage(index, value);
        }
        return this;
      }
      /**
       * <code>repeated .Container container = 7;</code>
       */
      public Builder addContainer(
          CK.Container.Builder builderForValue) {
        if (containerBuilder_ == null) {
          ensureContainerIsMutable();
          container_.add(builderForValue.build());
          onChanged();
        } else {
          containerBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .Container container = 7;</code>
       */
      public Builder addContainer(
          int index, CK.Container.Builder builderForValue) {
        if (containerBuilder_ == null) {
          ensureContainerIsMutable();
          container_.add(index, builderForValue.build());
          onChanged();
        } else {
          containerBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .Container container = 7;</code>
       */
      public Builder addAllContainer(
          java.lang.Iterable<? extends CK.Container> values) {
        if (containerBuilder_ == null) {
          ensureContainerIsMutable();
          com.google.protobuf.AbstractMessageLite.Builder.addAll(
              values, container_);
          onChanged();
        } else {
          containerBuilder_.addAllMessages(values);
        }
        return this;
      }
      /**
       * <code>repeated .Container container = 7;</code>
       */
      public Builder clearContainer() {
        if (containerBuilder_ == null) {
          container_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000020);
          onChanged();
        } else {
          containerBuilder_.clear();
        }
        return this;
      }
      /**
       * <code>repeated .Container container = 7;</code>
       */
      public Builder removeContainer(int index) {
        if (containerBuilder_ == null) {
          ensureContainerIsMutable();
          container_.remove(index);
          onChanged();
        } else {
          containerBuilder_.remove(index);
        }
        return this;
      }
      /**
       * <code>repeated .Container container = 7;</code>
       */
      public CK.Container.Builder getContainerBuilder(
          int index) {
        return getContainerFieldBuilder().getBuilder(index);
      }
      /**
       * <code>repeated .Container container = 7;</code>
       */
      public CK.ContainerOrBuilder getContainerOrBuilder(
          int index) {
        if (containerBuilder_ == null) {
          return container_.get(index);  } else {
          return containerBuilder_.getMessageOrBuilder(index);
        }
      }
      /**
       * <code>repeated .Container container = 7;</code>
       */
      public java.util.List<? extends CK.ContainerOrBuilder> 
           getContainerOrBuilderList() {
        if (containerBuilder_ != null) {
          return containerBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(container_);
        }
      }
      /**
       * <code>repeated .Container container = 7;</code>
       */
      public CK.Container.Builder addContainerBuilder() {
        return getContainerFieldBuilder().addBuilder(CK.Container.getDefaultInstance());
      }
      /**
       * <code>repeated .Container container = 7;</code>
       */
      public CK.Container.Builder addContainerBuilder(
          int index) {
        return getContainerFieldBuilder().addBuilder(index, CK.Container.getDefaultInstance());
      }
      /**
       * <code>repeated .Container container = 7;</code>
       */
      public java.util.List<CK.Container.Builder> 
           getContainerBuilderList() {
        return getContainerFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          CK.Container, CK.Container.Builder, CK.ContainerOrBuilder> 
          getContainerFieldBuilder() {
        if (containerBuilder_ == null) {
          containerBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              CK.Container, CK.Container.Builder, CK.ContainerOrBuilder>(
                  container_,
                  ((bitField0_ & 0x00000020) == 0x00000020),
                  getParentForChildren(),
                  isClean());
          container_ = null;
        }
        return containerBuilder_;
      }

      private CK.Item ckUserID_ = CK.Item.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Item, CK.Item.Builder, CK.ItemOrBuilder> ckUserIDBuilder_;
      /**
       * <code>optional .Item ckUserID = 9;</code>
       */
      public boolean hasCkUserID() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }
      /**
       * <code>optional .Item ckUserID = 9;</code>
       */
      public CK.Item getCkUserID() {
        if (ckUserIDBuilder_ == null) {
          return ckUserID_;
        } else {
          return ckUserIDBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .Item ckUserID = 9;</code>
       */
      public Builder setCkUserID(CK.Item value) {
        if (ckUserIDBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ckUserID_ = value;
          onChanged();
        } else {
          ckUserIDBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000040;
        return this;
      }
      /**
       * <code>optional .Item ckUserID = 9;</code>
       */
      public Builder setCkUserID(
          CK.Item.Builder builderForValue) {
        if (ckUserIDBuilder_ == null) {
          ckUserID_ = builderForValue.build();
          onChanged();
        } else {
          ckUserIDBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000040;
        return this;
      }
      /**
       * <code>optional .Item ckUserID = 9;</code>
       */
      public Builder mergeCkUserID(CK.Item value) {
        if (ckUserIDBuilder_ == null) {
          if (((bitField0_ & 0x00000040) == 0x00000040) &&
              ckUserID_ != CK.Item.getDefaultInstance()) {
            ckUserID_ =
              CK.Item.newBuilder(ckUserID_).mergeFrom(value).buildPartial();
          } else {
            ckUserID_ = value;
          }
          onChanged();
        } else {
          ckUserIDBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000040;
        return this;
      }
      /**
       * <code>optional .Item ckUserID = 9;</code>
       */
      public Builder clearCkUserID() {
        if (ckUserIDBuilder_ == null) {
          ckUserID_ = CK.Item.getDefaultInstance();
          onChanged();
        } else {
          ckUserIDBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000040);
        return this;
      }
      /**
       * <code>optional .Item ckUserID = 9;</code>
       */
      public CK.Item.Builder getCkUserIDBuilder() {
        bitField0_ |= 0x00000040;
        onChanged();
        return getCkUserIDFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Item ckUserID = 9;</code>
       */
      public CK.ItemOrBuilder getCkUserIDOrBuilder() {
        if (ckUserIDBuilder_ != null) {
          return ckUserIDBuilder_.getMessageOrBuilder();
        } else {
          return ckUserID_;
        }
      }
      /**
       * <code>optional .Item ckUserID = 9;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Item, CK.Item.Builder, CK.ItemOrBuilder> 
          getCkUserIDFieldBuilder() {
        if (ckUserIDBuilder_ == null) {
          ckUserIDBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Item, CK.Item.Builder, CK.ItemOrBuilder>(
                  getCkUserID(),
                  getParentForChildren(),
                  isClean());
          ckUserID_ = null;
        }
        return ckUserIDBuilder_;
      }

      private java.lang.Object deviceName_ = "";
      /**
       * <code>optional string deviceName = 11;</code>
       */
      public boolean hasDeviceName() {
        return ((bitField0_ & 0x00000080) == 0x00000080);
      }
      /**
       * <code>optional string deviceName = 11;</code>
       */
      public java.lang.String getDeviceName() {
        java.lang.Object ref = deviceName_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            deviceName_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string deviceName = 11;</code>
       */
      public com.google.protobuf.ByteString
          getDeviceNameBytes() {
        java.lang.Object ref = deviceName_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          deviceName_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string deviceName = 11;</code>
       */
      public Builder setDeviceName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000080;
        deviceName_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string deviceName = 11;</code>
       */
      public Builder clearDeviceName() {
        bitField0_ = (bitField0_ & ~0x00000080);
        deviceName_ = getDefaultInstance().getDeviceName();
        onChanged();
        return this;
      }
      /**
       * <code>optional string deviceName = 11;</code>
       */
      public Builder setDeviceNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000080;
        deviceName_ = value;
        onChanged();
        return this;
      }

      private CK.BytesString f13_ = CK.BytesString.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.BytesString, CK.BytesString.Builder, CK.BytesStringOrBuilder> f13Builder_;
      /**
       * <code>optional .BytesString f13 = 13;</code>
       */
      public boolean hasF13() {
        return ((bitField0_ & 0x00000100) == 0x00000100);
      }
      /**
       * <code>optional .BytesString f13 = 13;</code>
       */
      public CK.BytesString getF13() {
        if (f13Builder_ == null) {
          return f13_;
        } else {
          return f13Builder_.getMessage();
        }
      }
      /**
       * <code>optional .BytesString f13 = 13;</code>
       */
      public Builder setF13(CK.BytesString value) {
        if (f13Builder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          f13_ = value;
          onChanged();
        } else {
          f13Builder_.setMessage(value);
        }
        bitField0_ |= 0x00000100;
        return this;
      }
      /**
       * <code>optional .BytesString f13 = 13;</code>
       */
      public Builder setF13(
          CK.BytesString.Builder builderForValue) {
        if (f13Builder_ == null) {
          f13_ = builderForValue.build();
          onChanged();
        } else {
          f13Builder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000100;
        return this;
      }
      /**
       * <code>optional .BytesString f13 = 13;</code>
       */
      public Builder mergeF13(CK.BytesString value) {
        if (f13Builder_ == null) {
          if (((bitField0_ & 0x00000100) == 0x00000100) &&
              f13_ != CK.BytesString.getDefaultInstance()) {
            f13_ =
              CK.BytesString.newBuilder(f13_).mergeFrom(value).buildPartial();
          } else {
            f13_ = value;
          }
          onChanged();
        } else {
          f13Builder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000100;
        return this;
      }
      /**
       * <code>optional .BytesString f13 = 13;</code>
       */
      public Builder clearF13() {
        if (f13Builder_ == null) {
          f13_ = CK.BytesString.getDefaultInstance();
          onChanged();
        } else {
          f13Builder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000100);
        return this;
      }
      /**
       * <code>optional .BytesString f13 = 13;</code>
       */
      public CK.BytesString.Builder getF13Builder() {
        bitField0_ |= 0x00000100;
        onChanged();
        return getF13FieldBuilder().getBuilder();
      }
      /**
       * <code>optional .BytesString f13 = 13;</code>
       */
      public CK.BytesStringOrBuilder getF13OrBuilder() {
        if (f13Builder_ != null) {
          return f13Builder_.getMessageOrBuilder();
        } else {
          return f13_;
        }
      }
      /**
       * <code>optional .BytesString f13 = 13;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.BytesString, CK.BytesString.Builder, CK.BytesStringOrBuilder> 
          getF13FieldBuilder() {
        if (f13Builder_ == null) {
          f13Builder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.BytesString, CK.BytesString.Builder, CK.BytesStringOrBuilder>(
                  getF13(),
                  getParentForChildren(),
                  isClean());
          f13_ = null;
        }
        return f13Builder_;
      }

      private int f15_ ;
      /**
       * <code>optional uint32 f15 = 15;</code>
       */
      public boolean hasF15() {
        return ((bitField0_ & 0x00000200) == 0x00000200);
      }
      /**
       * <code>optional uint32 f15 = 15;</code>
       */
      public int getF15() {
        return f15_;
      }
      /**
       * <code>optional uint32 f15 = 15;</code>
       */
      public Builder setF15(int value) {
        bitField0_ |= 0x00000200;
        f15_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 f15 = 15;</code>
       */
      public Builder clearF15() {
        bitField0_ = (bitField0_ & ~0x00000200);
        f15_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:M211ResponseBody)
    }

    static {
      defaultInstance = new M211ResponseBody(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:M211ResponseBody)
  }

  public interface StatusOrBuilder extends
      // @@protoc_insertion_point(interface_extends:Status)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional uint32 code = 1;</code>
     */
    boolean hasCode();
    /**
     * <code>optional uint32 code = 1;</code>
     */
    int getCode();

    /**
     * <code>optional .Error error = 2;</code>
     */
    boolean hasError();
    /**
     * <code>optional .Error error = 2;</code>
     */
    CK.Error getError();
    /**
     * <code>optional .Error error = 2;</code>
     */
    CK.ErrorOrBuilder getErrorOrBuilder();
  }
  /**
   * Protobuf type {@code Status}
   */
  public static final class Status extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:Status)
      StatusOrBuilder {
    // Use Status.newBuilder() to construct.
    private Status(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Status(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Status defaultInstance;
    public static Status getDefaultInstance() {
      return defaultInstance;
    }

    public Status getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Status(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
            case 8: {
              bitField0_ |= 0x00000001;
              code_ = input.readUInt32();
              break;
            }
            case 18: {
              CK.Error.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = error_.toBuilder();
              }
              error_ = input.readMessage(CK.Error.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(error_);
                error_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_Status_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_Status_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.Status.class, CK.Status.Builder.class);
    }

    public static com.google.protobuf.Parser<Status> PARSER =
        new com.google.protobuf.AbstractParser<Status>() {
      public Status parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Status(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Status> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int CODE_FIELD_NUMBER = 1;
    private int code_;
    /**
     * <code>optional uint32 code = 1;</code>
     */
    public boolean hasCode() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional uint32 code = 1;</code>
     */
    public int getCode() {
      return code_;
    }

    public static final int ERROR_FIELD_NUMBER = 2;
    private CK.Error error_;
    /**
     * <code>optional .Error error = 2;</code>
     */
    public boolean hasError() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional .Error error = 2;</code>
     */
    public CK.Error getError() {
      return error_;
    }
    /**
     * <code>optional .Error error = 2;</code>
     */
    public CK.ErrorOrBuilder getErrorOrBuilder() {
      return error_;
    }

    private void initFields() {
      code_ = 0;
      error_ = CK.Error.getDefaultInstance();
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeUInt32(1, code_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(2, error_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(1, code_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, error_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.Status parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Status parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Status parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Status parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Status parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Status parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.Status parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.Status parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.Status parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Status parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.Status prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code Status}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:Status)
        CK.StatusOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_Status_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_Status_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.Status.class, CK.Status.Builder.class);
      }

      // Construct using CloudKit.Status.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getErrorFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        code_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        if (errorBuilder_ == null) {
          error_ = CK.Error.getDefaultInstance();
        } else {
          errorBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_Status_descriptor;
      }

      public CK.Status getDefaultInstanceForType() {
        return CK.Status.getDefaultInstance();
      }

      public CK.Status build() {
        CK.Status result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.Status buildPartial() {
        CK.Status result = new CK.Status(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.code_ = code_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (errorBuilder_ == null) {
          result.error_ = error_;
        } else {
          result.error_ = errorBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.Status) {
          return mergeFrom((CK.Status)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.Status other) {
        if (other == CK.Status.getDefaultInstance()) return this;
        if (other.hasCode()) {
          setCode(other.getCode());
        }
        if (other.hasError()) {
          mergeError(other.getError());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.Status parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.Status) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int code_ ;
      /**
       * <code>optional uint32 code = 1;</code>
       */
      public boolean hasCode() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional uint32 code = 1;</code>
       */
      public int getCode() {
        return code_;
      }
      /**
       * <code>optional uint32 code = 1;</code>
       */
      public Builder setCode(int value) {
        bitField0_ |= 0x00000001;
        code_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 code = 1;</code>
       */
      public Builder clearCode() {
        bitField0_ = (bitField0_ & ~0x00000001);
        code_ = 0;
        onChanged();
        return this;
      }

      private CK.Error error_ = CK.Error.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Error, CK.Error.Builder, CK.ErrorOrBuilder> errorBuilder_;
      /**
       * <code>optional .Error error = 2;</code>
       */
      public boolean hasError() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional .Error error = 2;</code>
       */
      public CK.Error getError() {
        if (errorBuilder_ == null) {
          return error_;
        } else {
          return errorBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .Error error = 2;</code>
       */
      public Builder setError(CK.Error value) {
        if (errorBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          error_ = value;
          onChanged();
        } else {
          errorBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Error error = 2;</code>
       */
      public Builder setError(
          CK.Error.Builder builderForValue) {
        if (errorBuilder_ == null) {
          error_ = builderForValue.build();
          onChanged();
        } else {
          errorBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Error error = 2;</code>
       */
      public Builder mergeError(CK.Error value) {
        if (errorBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              error_ != CK.Error.getDefaultInstance()) {
            error_ =
              CK.Error.newBuilder(error_).mergeFrom(value).buildPartial();
          } else {
            error_ = value;
          }
          onChanged();
        } else {
          errorBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Error error = 2;</code>
       */
      public Builder clearError() {
        if (errorBuilder_ == null) {
          error_ = CK.Error.getDefaultInstance();
          onChanged();
        } else {
          errorBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      /**
       * <code>optional .Error error = 2;</code>
       */
      public CK.Error.Builder getErrorBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getErrorFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Error error = 2;</code>
       */
      public CK.ErrorOrBuilder getErrorOrBuilder() {
        if (errorBuilder_ != null) {
          return errorBuilder_.getMessageOrBuilder();
        } else {
          return error_;
        }
      }
      /**
       * <code>optional .Error error = 2;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Error, CK.Error.Builder, CK.ErrorOrBuilder> 
          getErrorFieldBuilder() {
        if (errorBuilder_ == null) {
          errorBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Error, CK.Error.Builder, CK.ErrorOrBuilder>(
                  getError(),
                  getParentForChildren(),
                  isClean());
          error_ = null;
        }
        return errorBuilder_;
      }

      // @@protoc_insertion_point(builder_scope:Status)
    }

    static {
      defaultInstance = new Status(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:Status)
  }

  public interface ErrorOrBuilder extends
      // @@protoc_insertion_point(interface_extends:Error)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional .UInt32 code = 1;</code>
     */
    boolean hasCode();
    /**
     * <code>optional .UInt32 code = 1;</code>
     */
    CK.UInt32 getCode();
    /**
     * <code>optional .UInt32 code = 1;</code>
     */
    CK.UInt32OrBuilder getCodeOrBuilder();

    /**
     * <code>optional string message = 4;</code>
     */
    boolean hasMessage();
    /**
     * <code>optional string message = 4;</code>
     */
    java.lang.String getMessage();
    /**
     * <code>optional string message = 4;</code>
     */
    com.google.protobuf.ByteString
        getMessageBytes();

    /**
     * <code>optional string id = 5;</code>
     */
    boolean hasId();
    /**
     * <code>optional string id = 5;</code>
     */
    java.lang.String getId();
    /**
     * <code>optional string id = 5;</code>
     */
    com.google.protobuf.ByteString
        getIdBytes();
  }
  /**
   * Protobuf type {@code Error}
   */
  public static final class Error extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:Error)
      ErrorOrBuilder {
    // Use Error.newBuilder() to construct.
    private Error(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Error(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Error defaultInstance;
    public static Error getDefaultInstance() {
      return defaultInstance;
    }

    public Error getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Error(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
              CK.UInt32.Builder subBuilder = null;
              if (((bitField0_ & 0x00000001) == 0x00000001)) {
                subBuilder = code_.toBuilder();
              }
              code_ = input.readMessage(CK.UInt32.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(code_);
                code_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000001;
              break;
            }
            case 34: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000002;
              message_ = bs;
              break;
            }
            case 42: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000004;
              id_ = bs;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_Error_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_Error_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.Error.class, CK.Error.Builder.class);
    }

    public static com.google.protobuf.Parser<Error> PARSER =
        new com.google.protobuf.AbstractParser<Error>() {
      public Error parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Error(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Error> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int CODE_FIELD_NUMBER = 1;
    private CK.UInt32 code_;
    /**
     * <code>optional .UInt32 code = 1;</code>
     */
    public boolean hasCode() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional .UInt32 code = 1;</code>
     */
    public CK.UInt32 getCode() {
      return code_;
    }
    /**
     * <code>optional .UInt32 code = 1;</code>
     */
    public CK.UInt32OrBuilder getCodeOrBuilder() {
      return code_;
    }

    public static final int MESSAGE_FIELD_NUMBER = 4;
    private java.lang.Object message_;
    /**
     * <code>optional string message = 4;</code>
     */
    public boolean hasMessage() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional string message = 4;</code>
     */
    public java.lang.String getMessage() {
      java.lang.Object ref = message_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          message_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string message = 4;</code>
     */
    public com.google.protobuf.ByteString
        getMessageBytes() {
      java.lang.Object ref = message_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        message_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int ID_FIELD_NUMBER = 5;
    private java.lang.Object id_;
    /**
     * <code>optional string id = 5;</code>
     */
    public boolean hasId() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional string id = 5;</code>
     */
    public java.lang.String getId() {
      java.lang.Object ref = id_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          id_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string id = 5;</code>
     */
    public com.google.protobuf.ByteString
        getIdBytes() {
      java.lang.Object ref = id_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        id_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private void initFields() {
      code_ = CK.UInt32.getDefaultInstance();
      message_ = "";
      id_ = "";
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeMessage(1, code_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(4, getMessageBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(5, getIdBytes());
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, code_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(4, getMessageBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(5, getIdBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.Error parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Error parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Error parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Error parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Error parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Error parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.Error parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.Error parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.Error parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Error parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.Error prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code Error}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:Error)
        CK.ErrorOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_Error_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_Error_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.Error.class, CK.Error.Builder.class);
      }

      // Construct using CloudKit.Error.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getCodeFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (codeBuilder_ == null) {
          code_ = CK.UInt32.getDefaultInstance();
        } else {
          codeBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        message_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        id_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_Error_descriptor;
      }

      public CK.Error getDefaultInstanceForType() {
        return CK.Error.getDefaultInstance();
      }

      public CK.Error build() {
        CK.Error result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.Error buildPartial() {
        CK.Error result = new CK.Error(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (codeBuilder_ == null) {
          result.code_ = code_;
        } else {
          result.code_ = codeBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.message_ = message_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.id_ = id_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.Error) {
          return mergeFrom((CK.Error)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.Error other) {
        if (other == CK.Error.getDefaultInstance()) return this;
        if (other.hasCode()) {
          mergeCode(other.getCode());
        }
        if (other.hasMessage()) {
          bitField0_ |= 0x00000002;
          message_ = other.message_;
          onChanged();
        }
        if (other.hasId()) {
          bitField0_ |= 0x00000004;
          id_ = other.id_;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.Error parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.Error) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private CK.UInt32 code_ = CK.UInt32.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.UInt32, CK.UInt32.Builder, CK.UInt32OrBuilder> codeBuilder_;
      /**
       * <code>optional .UInt32 code = 1;</code>
       */
      public boolean hasCode() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional .UInt32 code = 1;</code>
       */
      public CK.UInt32 getCode() {
        if (codeBuilder_ == null) {
          return code_;
        } else {
          return codeBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .UInt32 code = 1;</code>
       */
      public Builder setCode(CK.UInt32 value) {
        if (codeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          code_ = value;
          onChanged();
        } else {
          codeBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .UInt32 code = 1;</code>
       */
      public Builder setCode(
          CK.UInt32.Builder builderForValue) {
        if (codeBuilder_ == null) {
          code_ = builderForValue.build();
          onChanged();
        } else {
          codeBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .UInt32 code = 1;</code>
       */
      public Builder mergeCode(CK.UInt32 value) {
        if (codeBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              code_ != CK.UInt32.getDefaultInstance()) {
            code_ =
              CK.UInt32.newBuilder(code_).mergeFrom(value).buildPartial();
          } else {
            code_ = value;
          }
          onChanged();
        } else {
          codeBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .UInt32 code = 1;</code>
       */
      public Builder clearCode() {
        if (codeBuilder_ == null) {
          code_ = CK.UInt32.getDefaultInstance();
          onChanged();
        } else {
          codeBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      /**
       * <code>optional .UInt32 code = 1;</code>
       */
      public CK.UInt32.Builder getCodeBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getCodeFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .UInt32 code = 1;</code>
       */
      public CK.UInt32OrBuilder getCodeOrBuilder() {
        if (codeBuilder_ != null) {
          return codeBuilder_.getMessageOrBuilder();
        } else {
          return code_;
        }
      }
      /**
       * <code>optional .UInt32 code = 1;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.UInt32, CK.UInt32.Builder, CK.UInt32OrBuilder> 
          getCodeFieldBuilder() {
        if (codeBuilder_ == null) {
          codeBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.UInt32, CK.UInt32.Builder, CK.UInt32OrBuilder>(
                  getCode(),
                  getParentForChildren(),
                  isClean());
          code_ = null;
        }
        return codeBuilder_;
      }

      private java.lang.Object message_ = "";
      /**
       * <code>optional string message = 4;</code>
       */
      public boolean hasMessage() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional string message = 4;</code>
       */
      public java.lang.String getMessage() {
        java.lang.Object ref = message_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            message_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string message = 4;</code>
       */
      public com.google.protobuf.ByteString
          getMessageBytes() {
        java.lang.Object ref = message_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          message_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string message = 4;</code>
       */
      public Builder setMessage(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        message_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string message = 4;</code>
       */
      public Builder clearMessage() {
        bitField0_ = (bitField0_ & ~0x00000002);
        message_ = getDefaultInstance().getMessage();
        onChanged();
        return this;
      }
      /**
       * <code>optional string message = 4;</code>
       */
      public Builder setMessageBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        message_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object id_ = "";
      /**
       * <code>optional string id = 5;</code>
       */
      public boolean hasId() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional string id = 5;</code>
       */
      public java.lang.String getId() {
        java.lang.Object ref = id_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            id_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string id = 5;</code>
       */
      public com.google.protobuf.ByteString
          getIdBytes() {
        java.lang.Object ref = id_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          id_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string id = 5;</code>
       */
      public Builder setId(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        id_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string id = 5;</code>
       */
      public Builder clearId() {
        bitField0_ = (bitField0_ & ~0x00000004);
        id_ = getDefaultInstance().getId();
        onChanged();
        return this;
      }
      /**
       * <code>optional string id = 5;</code>
       */
      public Builder setIdBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        id_ = value;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:Error)
    }

    static {
      defaultInstance = new Error(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:Error)
  }

  public interface ContainerOrBuilder extends
      // @@protoc_insertion_point(interface_extends:Container)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional .String tag = 1;</code>
     */
    boolean hasTag();
    /**
     * <code>optional .String tag = 1;</code>
     */
    CK.String getTag();
    /**
     * <code>optional .String tag = 1;</code>
     */
    CK.StringOrBuilder getTagOrBuilder();

    /**
     * <code>optional .Data data = 2;</code>
     */
    boolean hasData();
    /**
     * <code>optional .Data data = 2;</code>
     */
    CK.Data getData();
    /**
     * <code>optional .Data data = 2;</code>
     */
    CK.DataOrBuilder getDataOrBuilder();
  }
  /**
   * Protobuf type {@code Container}
   *
   * <pre>
   * Record_Response_211_1_7
   * </pre>
   */
  public static final class Container extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:Container)
      ContainerOrBuilder {
    // Use Container.newBuilder() to construct.
    private Container(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Container(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Container defaultInstance;
    public static Container getDefaultInstance() {
      return defaultInstance;
    }

    public Container getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Container(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
              CK.String.Builder subBuilder = null;
              if (((bitField0_ & 0x00000001) == 0x00000001)) {
                subBuilder = tag_.toBuilder();
              }
              tag_ = input.readMessage(CK.String.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(tag_);
                tag_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000001;
              break;
            }
            case 18: {
              CK.Data.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = data_.toBuilder();
              }
              data_ = input.readMessage(CK.Data.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(data_);
                data_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_Container_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_Container_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.Container.class, CK.Container.Builder.class);
    }

    public static com.google.protobuf.Parser<Container> PARSER =
        new com.google.protobuf.AbstractParser<Container>() {
      public Container parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Container(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Container> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int TAG_FIELD_NUMBER = 1;
    private CK.String tag_;
    /**
     * <code>optional .String tag = 1;</code>
     */
    public boolean hasTag() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional .String tag = 1;</code>
     */
    public CK.String getTag() {
      return tag_;
    }
    /**
     * <code>optional .String tag = 1;</code>
     */
    public CK.StringOrBuilder getTagOrBuilder() {
      return tag_;
    }

    public static final int DATA_FIELD_NUMBER = 2;
    private CK.Data data_;
    /**
     * <code>optional .Data data = 2;</code>
     */
    public boolean hasData() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional .Data data = 2;</code>
     */
    public CK.Data getData() {
      return data_;
    }
    /**
     * <code>optional .Data data = 2;</code>
     */
    public CK.DataOrBuilder getDataOrBuilder() {
      return data_;
    }

    private void initFields() {
      tag_ = CK.String.getDefaultInstance();
      data_ = CK.Data.getDefaultInstance();
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeMessage(1, tag_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(2, data_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, tag_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, data_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.Container parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Container parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Container parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Container parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Container parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Container parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.Container parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.Container parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.Container parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Container parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.Container prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code Container}
     *
     * <pre>
     * Record_Response_211_1_7
     * </pre>
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:Container)
        CK.ContainerOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_Container_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_Container_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.Container.class, CK.Container.Builder.class);
      }

      // Construct using CloudKit.Container.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getTagFieldBuilder();
          getDataFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (tagBuilder_ == null) {
          tag_ = CK.String.getDefaultInstance();
        } else {
          tagBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        if (dataBuilder_ == null) {
          data_ = CK.Data.getDefaultInstance();
        } else {
          dataBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_Container_descriptor;
      }

      public CK.Container getDefaultInstanceForType() {
        return CK.Container.getDefaultInstance();
      }

      public CK.Container build() {
        CK.Container result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.Container buildPartial() {
        CK.Container result = new CK.Container(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (tagBuilder_ == null) {
          result.tag_ = tag_;
        } else {
          result.tag_ = tagBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (dataBuilder_ == null) {
          result.data_ = data_;
        } else {
          result.data_ = dataBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.Container) {
          return mergeFrom((CK.Container)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.Container other) {
        if (other == CK.Container.getDefaultInstance()) return this;
        if (other.hasTag()) {
          mergeTag(other.getTag());
        }
        if (other.hasData()) {
          mergeData(other.getData());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.Container parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.Container) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private CK.String tag_ = CK.String.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.String, CK.String.Builder, CK.StringOrBuilder> tagBuilder_;
      /**
       * <code>optional .String tag = 1;</code>
       */
      public boolean hasTag() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional .String tag = 1;</code>
       */
      public CK.String getTag() {
        if (tagBuilder_ == null) {
          return tag_;
        } else {
          return tagBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .String tag = 1;</code>
       */
      public Builder setTag(CK.String value) {
        if (tagBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          tag_ = value;
          onChanged();
        } else {
          tagBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .String tag = 1;</code>
       */
      public Builder setTag(
          CK.String.Builder builderForValue) {
        if (tagBuilder_ == null) {
          tag_ = builderForValue.build();
          onChanged();
        } else {
          tagBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .String tag = 1;</code>
       */
      public Builder mergeTag(CK.String value) {
        if (tagBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              tag_ != CK.String.getDefaultInstance()) {
            tag_ =
              CK.String.newBuilder(tag_).mergeFrom(value).buildPartial();
          } else {
            tag_ = value;
          }
          onChanged();
        } else {
          tagBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .String tag = 1;</code>
       */
      public Builder clearTag() {
        if (tagBuilder_ == null) {
          tag_ = CK.String.getDefaultInstance();
          onChanged();
        } else {
          tagBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      /**
       * <code>optional .String tag = 1;</code>
       */
      public CK.String.Builder getTagBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getTagFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .String tag = 1;</code>
       */
      public CK.StringOrBuilder getTagOrBuilder() {
        if (tagBuilder_ != null) {
          return tagBuilder_.getMessageOrBuilder();
        } else {
          return tag_;
        }
      }
      /**
       * <code>optional .String tag = 1;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.String, CK.String.Builder, CK.StringOrBuilder> 
          getTagFieldBuilder() {
        if (tagBuilder_ == null) {
          tagBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.String, CK.String.Builder, CK.StringOrBuilder>(
                  getTag(),
                  getParentForChildren(),
                  isClean());
          tag_ = null;
        }
        return tagBuilder_;
      }

      private CK.Data data_ = CK.Data.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Data, CK.Data.Builder, CK.DataOrBuilder> dataBuilder_;
      /**
       * <code>optional .Data data = 2;</code>
       */
      public boolean hasData() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional .Data data = 2;</code>
       */
      public CK.Data getData() {
        if (dataBuilder_ == null) {
          return data_;
        } else {
          return dataBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .Data data = 2;</code>
       */
      public Builder setData(CK.Data value) {
        if (dataBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          data_ = value;
          onChanged();
        } else {
          dataBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Data data = 2;</code>
       */
      public Builder setData(
          CK.Data.Builder builderForValue) {
        if (dataBuilder_ == null) {
          data_ = builderForValue.build();
          onChanged();
        } else {
          dataBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Data data = 2;</code>
       */
      public Builder mergeData(CK.Data value) {
        if (dataBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              data_ != CK.Data.getDefaultInstance()) {
            data_ =
              CK.Data.newBuilder(data_).mergeFrom(value).buildPartial();
          } else {
            data_ = value;
          }
          onChanged();
        } else {
          dataBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Data data = 2;</code>
       */
      public Builder clearData() {
        if (dataBuilder_ == null) {
          data_ = CK.Data.getDefaultInstance();
          onChanged();
        } else {
          dataBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      /**
       * <code>optional .Data data = 2;</code>
       */
      public CK.Data.Builder getDataBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getDataFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Data data = 2;</code>
       */
      public CK.DataOrBuilder getDataOrBuilder() {
        if (dataBuilder_ != null) {
          return dataBuilder_.getMessageOrBuilder();
        } else {
          return data_;
        }
      }
      /**
       * <code>optional .Data data = 2;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Data, CK.Data.Builder, CK.DataOrBuilder> 
          getDataFieldBuilder() {
        if (dataBuilder_ == null) {
          dataBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Data, CK.Data.Builder, CK.DataOrBuilder>(
                  getData(),
                  getParentForChildren(),
                  isClean());
          data_ = null;
        }
        return dataBuilder_;
      }

      // @@protoc_insertion_point(builder_scope:Container)
    }

    static {
      defaultInstance = new Container(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:Container)
  }

  public interface DataOrBuilder extends
      // @@protoc_insertion_point(interface_extends:Data)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional uint32 id = 1;</code>
     */
    boolean hasId();
    /**
     * <code>optional uint32 id = 1;</code>
     */
    int getId();

    /**
     * <code>optional bytes bytes = 2;</code>
     */
    boolean hasBytes();
    /**
     * <code>optional bytes bytes = 2;</code>
     */
    com.google.protobuf.ByteString getBytes();

    /**
     * <code>optional uint32 uint32 = 4;</code>
     */
    boolean hasUint32();
    /**
     * <code>optional uint32 uint32 = 4;</code>
     */
    int getUint32();

    /**
     * <code>optional .Fixed64 fixed64 = 6;</code>
     */
    boolean hasFixed64();
    /**
     * <code>optional .Fixed64 fixed64 = 6;</code>
     */
    CK.Fixed64 getFixed64();
    /**
     * <code>optional .Fixed64 fixed64 = 6;</code>
     */
    CK.Fixed64OrBuilder getFixed64OrBuilder();

    /**
     * <code>optional string string = 7;</code>
     */
    boolean hasString();
    /**
     * <code>optional string string = 7;</code>
     */
    java.lang.String getString();
    /**
     * <code>optional string string = 7;</code>
     */
    com.google.protobuf.ByteString
        getStringBytes();

    /**
     * <code>optional .IdItemOp idItemOp = 9;</code>
     */
    boolean hasIdItemOp();
    /**
     * <code>optional .IdItemOp idItemOp = 9;</code>
     */
    CK.IdItemOp getIdItemOp();
    /**
     * <code>optional .IdItemOp idItemOp = 9;</code>
     */
    CK.IdItemOpOrBuilder getIdItemOpOrBuilder();

    /**
     * <code>repeated .Data data = 11;</code>
     */
    java.util.List<CK.Data> 
        getDataList();
    /**
     * <code>repeated .Data data = 11;</code>
     */
    CK.Data getData(int index);
    /**
     * <code>repeated .Data data = 11;</code>
     */
    int getDataCount();
    /**
     * <code>repeated .Data data = 11;</code>
     */
    java.util.List<? extends CK.DataOrBuilder> 
        getDataOrBuilderList();
    /**
     * <code>repeated .Data data = 11;</code>
     */
    CK.DataOrBuilder getDataOrBuilder(
        int index);
  }
  /**
   * Protobuf type {@code Data}
   *
   * <pre>
   * Record_Response_211_1_7_2 Record_Response_211_1_7_2_11
   * </pre>
   */
  public static final class Data extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:Data)
      DataOrBuilder {
    // Use Data.newBuilder() to construct.
    private Data(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Data(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Data defaultInstance;
    public static Data getDefaultInstance() {
      return defaultInstance;
    }

    public Data getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Data(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
            case 8: {
              bitField0_ |= 0x00000001;
              id_ = input.readUInt32();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              bytes_ = input.readBytes();
              break;
            }
            case 32: {
              bitField0_ |= 0x00000004;
              uint32_ = input.readUInt32();
              break;
            }
            case 50: {
              CK.Fixed64.Builder subBuilder = null;
              if (((bitField0_ & 0x00000008) == 0x00000008)) {
                subBuilder = fixed64_.toBuilder();
              }
              fixed64_ = input.readMessage(CK.Fixed64.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(fixed64_);
                fixed64_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000008;
              break;
            }
            case 58: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000010;
              string_ = bs;
              break;
            }
            case 74: {
              CK.IdItemOp.Builder subBuilder = null;
              if (((bitField0_ & 0x00000020) == 0x00000020)) {
                subBuilder = idItemOp_.toBuilder();
              }
              idItemOp_ = input.readMessage(CK.IdItemOp.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(idItemOp_);
                idItemOp_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000020;
              break;
            }
            case 90: {
              if (!((mutable_bitField0_ & 0x00000040) == 0x00000040)) {
                data_ = new java.util.ArrayList<CK.Data>();
                mutable_bitField0_ |= 0x00000040;
              }
              data_.add(input.readMessage(CK.Data.PARSER, extensionRegistry));
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000040) == 0x00000040)) {
          data_ = java.util.Collections.unmodifiableList(data_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_Data_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_Data_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.Data.class, CK.Data.Builder.class);
    }

    public static com.google.protobuf.Parser<Data> PARSER =
        new com.google.protobuf.AbstractParser<Data>() {
      public Data parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Data(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Data> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int ID_FIELD_NUMBER = 1;
    private int id_;
    /**
     * <code>optional uint32 id = 1;</code>
     */
    public boolean hasId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional uint32 id = 1;</code>
     */
    public int getId() {
      return id_;
    }

    public static final int BYTES_FIELD_NUMBER = 2;
    private com.google.protobuf.ByteString bytes_;
    /**
     * <code>optional bytes bytes = 2;</code>
     */
    public boolean hasBytes() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional bytes bytes = 2;</code>
     */
    public com.google.protobuf.ByteString getBytes() {
      return bytes_;
    }

    public static final int UINT32_FIELD_NUMBER = 4;
    private int uint32_;
    /**
     * <code>optional uint32 uint32 = 4;</code>
     */
    public boolean hasUint32() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional uint32 uint32 = 4;</code>
     */
    public int getUint32() {
      return uint32_;
    }

    public static final int FIXED64_FIELD_NUMBER = 6;
    private CK.Fixed64 fixed64_;
    /**
     * <code>optional .Fixed64 fixed64 = 6;</code>
     */
    public boolean hasFixed64() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional .Fixed64 fixed64 = 6;</code>
     */
    public CK.Fixed64 getFixed64() {
      return fixed64_;
    }
    /**
     * <code>optional .Fixed64 fixed64 = 6;</code>
     */
    public CK.Fixed64OrBuilder getFixed64OrBuilder() {
      return fixed64_;
    }

    public static final int STRING_FIELD_NUMBER = 7;
    private java.lang.Object string_;
    /**
     * <code>optional string string = 7;</code>
     */
    public boolean hasString() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    /**
     * <code>optional string string = 7;</code>
     */
    public java.lang.String getString() {
      java.lang.Object ref = string_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          string_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string string = 7;</code>
     */
    public com.google.protobuf.ByteString
        getStringBytes() {
      java.lang.Object ref = string_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        string_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int IDITEMOP_FIELD_NUMBER = 9;
    private CK.IdItemOp idItemOp_;
    /**
     * <code>optional .IdItemOp idItemOp = 9;</code>
     */
    public boolean hasIdItemOp() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    /**
     * <code>optional .IdItemOp idItemOp = 9;</code>
     */
    public CK.IdItemOp getIdItemOp() {
      return idItemOp_;
    }
    /**
     * <code>optional .IdItemOp idItemOp = 9;</code>
     */
    public CK.IdItemOpOrBuilder getIdItemOpOrBuilder() {
      return idItemOp_;
    }

    public static final int DATA_FIELD_NUMBER = 11;
    private java.util.List<CK.Data> data_;
    /**
     * <code>repeated .Data data = 11;</code>
     */
    public java.util.List<CK.Data> getDataList() {
      return data_;
    }
    /**
     * <code>repeated .Data data = 11;</code>
     */
    public java.util.List<? extends CK.DataOrBuilder> 
        getDataOrBuilderList() {
      return data_;
    }
    /**
     * <code>repeated .Data data = 11;</code>
     */
    public int getDataCount() {
      return data_.size();
    }
    /**
     * <code>repeated .Data data = 11;</code>
     */
    public CK.Data getData(int index) {
      return data_.get(index);
    }
    /**
     * <code>repeated .Data data = 11;</code>
     */
    public CK.DataOrBuilder getDataOrBuilder(
        int index) {
      return data_.get(index);
    }

    private void initFields() {
      id_ = 0;
      bytes_ = com.google.protobuf.ByteString.EMPTY;
      uint32_ = 0;
      fixed64_ = CK.Fixed64.getDefaultInstance();
      string_ = "";
      idItemOp_ = CK.IdItemOp.getDefaultInstance();
      data_ = java.util.Collections.emptyList();
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeUInt32(1, id_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, bytes_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeUInt32(4, uint32_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeMessage(6, fixed64_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(7, getStringBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeMessage(9, idItemOp_);
      }
      for (int i = 0; i < data_.size(); i++) {
        output.writeMessage(11, data_.get(i));
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(1, id_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, bytes_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(4, uint32_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(6, fixed64_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(7, getStringBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(9, idItemOp_);
      }
      for (int i = 0; i < data_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(11, data_.get(i));
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.Data parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Data parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Data parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Data parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Data parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Data parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.Data parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.Data parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.Data parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Data parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.Data prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code Data}
     *
     * <pre>
     * Record_Response_211_1_7_2 Record_Response_211_1_7_2_11
     * </pre>
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:Data)
        CK.DataOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_Data_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_Data_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.Data.class, CK.Data.Builder.class);
      }

      // Construct using CloudKit.Data.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getFixed64FieldBuilder();
          getIdItemOpFieldBuilder();
          getDataFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        id_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        bytes_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000002);
        uint32_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        if (fixed64Builder_ == null) {
          fixed64_ = CK.Fixed64.getDefaultInstance();
        } else {
          fixed64Builder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        string_ = "";
        bitField0_ = (bitField0_ & ~0x00000010);
        if (idItemOpBuilder_ == null) {
          idItemOp_ = CK.IdItemOp.getDefaultInstance();
        } else {
          idItemOpBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000020);
        if (dataBuilder_ == null) {
          data_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000040);
        } else {
          dataBuilder_.clear();
        }
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_Data_descriptor;
      }

      public CK.Data getDefaultInstanceForType() {
        return CK.Data.getDefaultInstance();
      }

      public CK.Data build() {
        CK.Data result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.Data buildPartial() {
        CK.Data result = new CK.Data(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.id_ = id_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.bytes_ = bytes_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.uint32_ = uint32_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        if (fixed64Builder_ == null) {
          result.fixed64_ = fixed64_;
        } else {
          result.fixed64_ = fixed64Builder_.build();
        }
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.string_ = string_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        if (idItemOpBuilder_ == null) {
          result.idItemOp_ = idItemOp_;
        } else {
          result.idItemOp_ = idItemOpBuilder_.build();
        }
        if (dataBuilder_ == null) {
          if (((bitField0_ & 0x00000040) == 0x00000040)) {
            data_ = java.util.Collections.unmodifiableList(data_);
            bitField0_ = (bitField0_ & ~0x00000040);
          }
          result.data_ = data_;
        } else {
          result.data_ = dataBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.Data) {
          return mergeFrom((CK.Data)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.Data other) {
        if (other == CK.Data.getDefaultInstance()) return this;
        if (other.hasId()) {
          setId(other.getId());
        }
        if (other.hasBytes()) {
          setBytes(other.getBytes());
        }
        if (other.hasUint32()) {
          setUint32(other.getUint32());
        }
        if (other.hasFixed64()) {
          mergeFixed64(other.getFixed64());
        }
        if (other.hasString()) {
          bitField0_ |= 0x00000010;
          string_ = other.string_;
          onChanged();
        }
        if (other.hasIdItemOp()) {
          mergeIdItemOp(other.getIdItemOp());
        }
        if (dataBuilder_ == null) {
          if (!other.data_.isEmpty()) {
            if (data_.isEmpty()) {
              data_ = other.data_;
              bitField0_ = (bitField0_ & ~0x00000040);
            } else {
              ensureDataIsMutable();
              data_.addAll(other.data_);
            }
            onChanged();
          }
        } else {
          if (!other.data_.isEmpty()) {
            if (dataBuilder_.isEmpty()) {
              dataBuilder_.dispose();
              dataBuilder_ = null;
              data_ = other.data_;
              bitField0_ = (bitField0_ & ~0x00000040);
              dataBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getDataFieldBuilder() : null;
            } else {
              dataBuilder_.addAllMessages(other.data_);
            }
          }
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.Data parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.Data) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int id_ ;
      /**
       * <code>optional uint32 id = 1;</code>
       */
      public boolean hasId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional uint32 id = 1;</code>
       */
      public int getId() {
        return id_;
      }
      /**
       * <code>optional uint32 id = 1;</code>
       */
      public Builder setId(int value) {
        bitField0_ |= 0x00000001;
        id_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 id = 1;</code>
       */
      public Builder clearId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        id_ = 0;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString bytes_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes bytes = 2;</code>
       */
      public boolean hasBytes() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional bytes bytes = 2;</code>
       */
      public com.google.protobuf.ByteString getBytes() {
        return bytes_;
      }
      /**
       * <code>optional bytes bytes = 2;</code>
       */
      public Builder setBytes(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        bytes_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes bytes = 2;</code>
       */
      public Builder clearBytes() {
        bitField0_ = (bitField0_ & ~0x00000002);
        bytes_ = getDefaultInstance().getBytes();
        onChanged();
        return this;
      }

      private int uint32_ ;
      /**
       * <code>optional uint32 uint32 = 4;</code>
       */
      public boolean hasUint32() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional uint32 uint32 = 4;</code>
       */
      public int getUint32() {
        return uint32_;
      }
      /**
       * <code>optional uint32 uint32 = 4;</code>
       */
      public Builder setUint32(int value) {
        bitField0_ |= 0x00000004;
        uint32_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 uint32 = 4;</code>
       */
      public Builder clearUint32() {
        bitField0_ = (bitField0_ & ~0x00000004);
        uint32_ = 0;
        onChanged();
        return this;
      }

      private CK.Fixed64 fixed64_ = CK.Fixed64.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Fixed64, CK.Fixed64.Builder, CK.Fixed64OrBuilder> fixed64Builder_;
      /**
       * <code>optional .Fixed64 fixed64 = 6;</code>
       */
      public boolean hasFixed64() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>optional .Fixed64 fixed64 = 6;</code>
       */
      public CK.Fixed64 getFixed64() {
        if (fixed64Builder_ == null) {
          return fixed64_;
        } else {
          return fixed64Builder_.getMessage();
        }
      }
      /**
       * <code>optional .Fixed64 fixed64 = 6;</code>
       */
      public Builder setFixed64(CK.Fixed64 value) {
        if (fixed64Builder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          fixed64_ = value;
          onChanged();
        } else {
          fixed64Builder_.setMessage(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .Fixed64 fixed64 = 6;</code>
       */
      public Builder setFixed64(
          CK.Fixed64.Builder builderForValue) {
        if (fixed64Builder_ == null) {
          fixed64_ = builderForValue.build();
          onChanged();
        } else {
          fixed64Builder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .Fixed64 fixed64 = 6;</code>
       */
      public Builder mergeFixed64(CK.Fixed64 value) {
        if (fixed64Builder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008) &&
              fixed64_ != CK.Fixed64.getDefaultInstance()) {
            fixed64_ =
              CK.Fixed64.newBuilder(fixed64_).mergeFrom(value).buildPartial();
          } else {
            fixed64_ = value;
          }
          onChanged();
        } else {
          fixed64Builder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .Fixed64 fixed64 = 6;</code>
       */
      public Builder clearFixed64() {
        if (fixed64Builder_ == null) {
          fixed64_ = CK.Fixed64.getDefaultInstance();
          onChanged();
        } else {
          fixed64Builder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      /**
       * <code>optional .Fixed64 fixed64 = 6;</code>
       */
      public CK.Fixed64.Builder getFixed64Builder() {
        bitField0_ |= 0x00000008;
        onChanged();
        return getFixed64FieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Fixed64 fixed64 = 6;</code>
       */
      public CK.Fixed64OrBuilder getFixed64OrBuilder() {
        if (fixed64Builder_ != null) {
          return fixed64Builder_.getMessageOrBuilder();
        } else {
          return fixed64_;
        }
      }
      /**
       * <code>optional .Fixed64 fixed64 = 6;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Fixed64, CK.Fixed64.Builder, CK.Fixed64OrBuilder> 
          getFixed64FieldBuilder() {
        if (fixed64Builder_ == null) {
          fixed64Builder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Fixed64, CK.Fixed64.Builder, CK.Fixed64OrBuilder>(
                  getFixed64(),
                  getParentForChildren(),
                  isClean());
          fixed64_ = null;
        }
        return fixed64Builder_;
      }

      private java.lang.Object string_ = "";
      /**
       * <code>optional string string = 7;</code>
       */
      public boolean hasString() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      /**
       * <code>optional string string = 7;</code>
       */
      public java.lang.String getString() {
        java.lang.Object ref = string_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            string_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string string = 7;</code>
       */
      public com.google.protobuf.ByteString
          getStringBytes() {
        java.lang.Object ref = string_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          string_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string string = 7;</code>
       */
      public Builder setString(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        string_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string string = 7;</code>
       */
      public Builder clearString() {
        bitField0_ = (bitField0_ & ~0x00000010);
        string_ = getDefaultInstance().getString();
        onChanged();
        return this;
      }
      /**
       * <code>optional string string = 7;</code>
       */
      public Builder setStringBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        string_ = value;
        onChanged();
        return this;
      }

      private CK.IdItemOp idItemOp_ = CK.IdItemOp.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.IdItemOp, CK.IdItemOp.Builder, CK.IdItemOpOrBuilder> idItemOpBuilder_;
      /**
       * <code>optional .IdItemOp idItemOp = 9;</code>
       */
      public boolean hasIdItemOp() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      /**
       * <code>optional .IdItemOp idItemOp = 9;</code>
       */
      public CK.IdItemOp getIdItemOp() {
        if (idItemOpBuilder_ == null) {
          return idItemOp_;
        } else {
          return idItemOpBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .IdItemOp idItemOp = 9;</code>
       */
      public Builder setIdItemOp(CK.IdItemOp value) {
        if (idItemOpBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          idItemOp_ = value;
          onChanged();
        } else {
          idItemOpBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000020;
        return this;
      }
      /**
       * <code>optional .IdItemOp idItemOp = 9;</code>
       */
      public Builder setIdItemOp(
          CK.IdItemOp.Builder builderForValue) {
        if (idItemOpBuilder_ == null) {
          idItemOp_ = builderForValue.build();
          onChanged();
        } else {
          idItemOpBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000020;
        return this;
      }
      /**
       * <code>optional .IdItemOp idItemOp = 9;</code>
       */
      public Builder mergeIdItemOp(CK.IdItemOp value) {
        if (idItemOpBuilder_ == null) {
          if (((bitField0_ & 0x00000020) == 0x00000020) &&
              idItemOp_ != CK.IdItemOp.getDefaultInstance()) {
            idItemOp_ =
              CK.IdItemOp.newBuilder(idItemOp_).mergeFrom(value).buildPartial();
          } else {
            idItemOp_ = value;
          }
          onChanged();
        } else {
          idItemOpBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000020;
        return this;
      }
      /**
       * <code>optional .IdItemOp idItemOp = 9;</code>
       */
      public Builder clearIdItemOp() {
        if (idItemOpBuilder_ == null) {
          idItemOp_ = CK.IdItemOp.getDefaultInstance();
          onChanged();
        } else {
          idItemOpBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000020);
        return this;
      }
      /**
       * <code>optional .IdItemOp idItemOp = 9;</code>
       */
      public CK.IdItemOp.Builder getIdItemOpBuilder() {
        bitField0_ |= 0x00000020;
        onChanged();
        return getIdItemOpFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .IdItemOp idItemOp = 9;</code>
       */
      public CK.IdItemOpOrBuilder getIdItemOpOrBuilder() {
        if (idItemOpBuilder_ != null) {
          return idItemOpBuilder_.getMessageOrBuilder();
        } else {
          return idItemOp_;
        }
      }
      /**
       * <code>optional .IdItemOp idItemOp = 9;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.IdItemOp, CK.IdItemOp.Builder, CK.IdItemOpOrBuilder> 
          getIdItemOpFieldBuilder() {
        if (idItemOpBuilder_ == null) {
          idItemOpBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.IdItemOp, CK.IdItemOp.Builder, CK.IdItemOpOrBuilder>(
                  getIdItemOp(),
                  getParentForChildren(),
                  isClean());
          idItemOp_ = null;
        }
        return idItemOpBuilder_;
      }

      private java.util.List<CK.Data> data_ =
        java.util.Collections.emptyList();
      private void ensureDataIsMutable() {
        if (!((bitField0_ & 0x00000040) == 0x00000040)) {
          data_ = new java.util.ArrayList<CK.Data>(data_);
          bitField0_ |= 0x00000040;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          CK.Data, CK.Data.Builder, CK.DataOrBuilder> dataBuilder_;

      /**
       * <code>repeated .Data data = 11;</code>
       */
      public java.util.List<CK.Data> getDataList() {
        if (dataBuilder_ == null) {
          return java.util.Collections.unmodifiableList(data_);
        } else {
          return dataBuilder_.getMessageList();
        }
      }
      /**
       * <code>repeated .Data data = 11;</code>
       */
      public int getDataCount() {
        if (dataBuilder_ == null) {
          return data_.size();
        } else {
          return dataBuilder_.getCount();
        }
      }
      /**
       * <code>repeated .Data data = 11;</code>
       */
      public CK.Data getData(int index) {
        if (dataBuilder_ == null) {
          return data_.get(index);
        } else {
          return dataBuilder_.getMessage(index);
        }
      }
      /**
       * <code>repeated .Data data = 11;</code>
       */
      public Builder setData(
          int index, CK.Data value) {
        if (dataBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureDataIsMutable();
          data_.set(index, value);
          onChanged();
        } else {
          dataBuilder_.setMessage(index, value);
        }
        return this;
      }
      /**
       * <code>repeated .Data data = 11;</code>
       */
      public Builder setData(
          int index, CK.Data.Builder builderForValue) {
        if (dataBuilder_ == null) {
          ensureDataIsMutable();
          data_.set(index, builderForValue.build());
          onChanged();
        } else {
          dataBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .Data data = 11;</code>
       */
      public Builder addData(CK.Data value) {
        if (dataBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureDataIsMutable();
          data_.add(value);
          onChanged();
        } else {
          dataBuilder_.addMessage(value);
        }
        return this;
      }
      /**
       * <code>repeated .Data data = 11;</code>
       */
      public Builder addData(
          int index, CK.Data value) {
        if (dataBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureDataIsMutable();
          data_.add(index, value);
          onChanged();
        } else {
          dataBuilder_.addMessage(index, value);
        }
        return this;
      }
      /**
       * <code>repeated .Data data = 11;</code>
       */
      public Builder addData(
          CK.Data.Builder builderForValue) {
        if (dataBuilder_ == null) {
          ensureDataIsMutable();
          data_.add(builderForValue.build());
          onChanged();
        } else {
          dataBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .Data data = 11;</code>
       */
      public Builder addData(
          int index, CK.Data.Builder builderForValue) {
        if (dataBuilder_ == null) {
          ensureDataIsMutable();
          data_.add(index, builderForValue.build());
          onChanged();
        } else {
          dataBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .Data data = 11;</code>
       */
      public Builder addAllData(
          java.lang.Iterable<? extends CK.Data> values) {
        if (dataBuilder_ == null) {
          ensureDataIsMutable();
          com.google.protobuf.AbstractMessageLite.Builder.addAll(
              values, data_);
          onChanged();
        } else {
          dataBuilder_.addAllMessages(values);
        }
        return this;
      }
      /**
       * <code>repeated .Data data = 11;</code>
       */
      public Builder clearData() {
        if (dataBuilder_ == null) {
          data_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000040);
          onChanged();
        } else {
          dataBuilder_.clear();
        }
        return this;
      }
      /**
       * <code>repeated .Data data = 11;</code>
       */
      public Builder removeData(int index) {
        if (dataBuilder_ == null) {
          ensureDataIsMutable();
          data_.remove(index);
          onChanged();
        } else {
          dataBuilder_.remove(index);
        }
        return this;
      }
      /**
       * <code>repeated .Data data = 11;</code>
       */
      public CK.Data.Builder getDataBuilder(
          int index) {
        return getDataFieldBuilder().getBuilder(index);
      }
      /**
       * <code>repeated .Data data = 11;</code>
       */
      public CK.DataOrBuilder getDataOrBuilder(
          int index) {
        if (dataBuilder_ == null) {
          return data_.get(index);  } else {
          return dataBuilder_.getMessageOrBuilder(index);
        }
      }
      /**
       * <code>repeated .Data data = 11;</code>
       */
      public java.util.List<? extends CK.DataOrBuilder> 
           getDataOrBuilderList() {
        if (dataBuilder_ != null) {
          return dataBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(data_);
        }
      }
      /**
       * <code>repeated .Data data = 11;</code>
       */
      public CK.Data.Builder addDataBuilder() {
        return getDataFieldBuilder().addBuilder(CK.Data.getDefaultInstance());
      }
      /**
       * <code>repeated .Data data = 11;</code>
       */
      public CK.Data.Builder addDataBuilder(
          int index) {
        return getDataFieldBuilder().addBuilder(index, CK.Data.getDefaultInstance());
      }
      /**
       * <code>repeated .Data data = 11;</code>
       */
      public java.util.List<CK.Data.Builder> 
           getDataBuilderList() {
        return getDataFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          CK.Data, CK.Data.Builder, CK.DataOrBuilder> 
          getDataFieldBuilder() {
        if (dataBuilder_ == null) {
          dataBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              CK.Data, CK.Data.Builder, CK.DataOrBuilder>(
                  data_,
                  ((bitField0_ & 0x00000040) == 0x00000040),
                  getParentForChildren(),
                  isClean());
          data_ = null;
        }
        return dataBuilder_;
      }

      // @@protoc_insertion_point(builder_scope:Data)
    }

    static {
      defaultInstance = new Data(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:Data)
  }

  public interface IdItemOpOrBuilder extends
      // @@protoc_insertion_point(interface_extends:IdItemOp)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional uint32 id = 1;</code>
     */
    boolean hasId();
    /**
     * <code>optional uint32 id = 1;</code>
     */
    int getId();

    /**
     * <code>optional .ItemOp itemOp = 2;</code>
     */
    boolean hasItemOp();
    /**
     * <code>optional .ItemOp itemOp = 2;</code>
     */
    CK.ItemOp getItemOp();
    /**
     * <code>optional .ItemOp itemOp = 2;</code>
     */
    CK.ItemOpOrBuilder getItemOpOrBuilder();
  }
  /**
   * Protobuf type {@code IdItemOp}
   *
   * <pre>
   * Record_Response_211_1_7_2_11_9
   * </pre>
   */
  public static final class IdItemOp extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:IdItemOp)
      IdItemOpOrBuilder {
    // Use IdItemOp.newBuilder() to construct.
    private IdItemOp(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private IdItemOp(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final IdItemOp defaultInstance;
    public static IdItemOp getDefaultInstance() {
      return defaultInstance;
    }

    public IdItemOp getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private IdItemOp(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
            case 8: {
              bitField0_ |= 0x00000001;
              id_ = input.readUInt32();
              break;
            }
            case 18: {
              CK.ItemOp.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = itemOp_.toBuilder();
              }
              itemOp_ = input.readMessage(CK.ItemOp.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(itemOp_);
                itemOp_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_IdItemOp_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_IdItemOp_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.IdItemOp.class, CK.IdItemOp.Builder.class);
    }

    public static com.google.protobuf.Parser<IdItemOp> PARSER =
        new com.google.protobuf.AbstractParser<IdItemOp>() {
      public IdItemOp parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new IdItemOp(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<IdItemOp> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int ID_FIELD_NUMBER = 1;
    private int id_;
    /**
     * <code>optional uint32 id = 1;</code>
     */
    public boolean hasId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional uint32 id = 1;</code>
     */
    public int getId() {
      return id_;
    }

    public static final int ITEMOP_FIELD_NUMBER = 2;
    private CK.ItemOp itemOp_;
    /**
     * <code>optional .ItemOp itemOp = 2;</code>
     */
    public boolean hasItemOp() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional .ItemOp itemOp = 2;</code>
     */
    public CK.ItemOp getItemOp() {
      return itemOp_;
    }
    /**
     * <code>optional .ItemOp itemOp = 2;</code>
     */
    public CK.ItemOpOrBuilder getItemOpOrBuilder() {
      return itemOp_;
    }

    private void initFields() {
      id_ = 0;
      itemOp_ = CK.ItemOp.getDefaultInstance();
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeUInt32(1, id_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(2, itemOp_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(1, id_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, itemOp_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.IdItemOp parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.IdItemOp parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.IdItemOp parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.IdItemOp parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.IdItemOp parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.IdItemOp parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.IdItemOp parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.IdItemOp parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.IdItemOp parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.IdItemOp parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.IdItemOp prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code IdItemOp}
     *
     * <pre>
     * Record_Response_211_1_7_2_11_9
     * </pre>
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:IdItemOp)
        CK.IdItemOpOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_IdItemOp_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_IdItemOp_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.IdItemOp.class, CK.IdItemOp.Builder.class);
      }

      // Construct using CloudKit.IdItemOp.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getItemOpFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        id_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        if (itemOpBuilder_ == null) {
          itemOp_ = CK.ItemOp.getDefaultInstance();
        } else {
          itemOpBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_IdItemOp_descriptor;
      }

      public CK.IdItemOp getDefaultInstanceForType() {
        return CK.IdItemOp.getDefaultInstance();
      }

      public CK.IdItemOp build() {
        CK.IdItemOp result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.IdItemOp buildPartial() {
        CK.IdItemOp result = new CK.IdItemOp(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.id_ = id_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (itemOpBuilder_ == null) {
          result.itemOp_ = itemOp_;
        } else {
          result.itemOp_ = itemOpBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.IdItemOp) {
          return mergeFrom((CK.IdItemOp)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.IdItemOp other) {
        if (other == CK.IdItemOp.getDefaultInstance()) return this;
        if (other.hasId()) {
          setId(other.getId());
        }
        if (other.hasItemOp()) {
          mergeItemOp(other.getItemOp());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.IdItemOp parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.IdItemOp) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int id_ ;
      /**
       * <code>optional uint32 id = 1;</code>
       */
      public boolean hasId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional uint32 id = 1;</code>
       */
      public int getId() {
        return id_;
      }
      /**
       * <code>optional uint32 id = 1;</code>
       */
      public Builder setId(int value) {
        bitField0_ |= 0x00000001;
        id_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 id = 1;</code>
       */
      public Builder clearId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        id_ = 0;
        onChanged();
        return this;
      }

      private CK.ItemOp itemOp_ = CK.ItemOp.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.ItemOp, CK.ItemOp.Builder, CK.ItemOpOrBuilder> itemOpBuilder_;
      /**
       * <code>optional .ItemOp itemOp = 2;</code>
       */
      public boolean hasItemOp() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional .ItemOp itemOp = 2;</code>
       */
      public CK.ItemOp getItemOp() {
        if (itemOpBuilder_ == null) {
          return itemOp_;
        } else {
          return itemOpBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .ItemOp itemOp = 2;</code>
       */
      public Builder setItemOp(CK.ItemOp value) {
        if (itemOpBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          itemOp_ = value;
          onChanged();
        } else {
          itemOpBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .ItemOp itemOp = 2;</code>
       */
      public Builder setItemOp(
          CK.ItemOp.Builder builderForValue) {
        if (itemOpBuilder_ == null) {
          itemOp_ = builderForValue.build();
          onChanged();
        } else {
          itemOpBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .ItemOp itemOp = 2;</code>
       */
      public Builder mergeItemOp(CK.ItemOp value) {
        if (itemOpBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              itemOp_ != CK.ItemOp.getDefaultInstance()) {
            itemOp_ =
              CK.ItemOp.newBuilder(itemOp_).mergeFrom(value).buildPartial();
          } else {
            itemOp_ = value;
          }
          onChanged();
        } else {
          itemOpBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .ItemOp itemOp = 2;</code>
       */
      public Builder clearItemOp() {
        if (itemOpBuilder_ == null) {
          itemOp_ = CK.ItemOp.getDefaultInstance();
          onChanged();
        } else {
          itemOpBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      /**
       * <code>optional .ItemOp itemOp = 2;</code>
       */
      public CK.ItemOp.Builder getItemOpBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getItemOpFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .ItemOp itemOp = 2;</code>
       */
      public CK.ItemOpOrBuilder getItemOpOrBuilder() {
        if (itemOpBuilder_ != null) {
          return itemOpBuilder_.getMessageOrBuilder();
        } else {
          return itemOp_;
        }
      }
      /**
       * <code>optional .ItemOp itemOp = 2;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.ItemOp, CK.ItemOp.Builder, CK.ItemOpOrBuilder> 
          getItemOpFieldBuilder() {
        if (itemOpBuilder_ == null) {
          itemOpBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.ItemOp, CK.ItemOp.Builder, CK.ItemOpOrBuilder>(
                  getItemOp(),
                  getParentForChildren(),
                  isClean());
          itemOp_ = null;
        }
        return itemOpBuilder_;
      }

      // @@protoc_insertion_point(builder_scope:IdItemOp)
    }

    static {
      defaultInstance = new IdItemOp(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:IdItemOp)
  }

  public interface ItemOpOrBuilder extends
      // @@protoc_insertion_point(interface_extends:ItemOp)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional .Item item = 1;</code>
     */
    boolean hasItem();
    /**
     * <code>optional .Item item = 1;</code>
     */
    CK.Item getItem();
    /**
     * <code>optional .Item item = 1;</code>
     */
    CK.ItemOrBuilder getItemOrBuilder();

    /**
     * <code>optional .Op op = 2;</code>
     */
    boolean hasOp();
    /**
     * <code>optional .Op op = 2;</code>
     */
    CK.Op getOp();
    /**
     * <code>optional .Op op = 2;</code>
     */
    CK.OpOrBuilder getOpOrBuilder();
  }
  /**
   * Protobuf type {@code ItemOp}
   */
  public static final class ItemOp extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:ItemOp)
      ItemOpOrBuilder {
    // Use ItemOp.newBuilder() to construct.
    private ItemOp(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private ItemOp(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final ItemOp defaultInstance;
    public static ItemOp getDefaultInstance() {
      return defaultInstance;
    }

    public ItemOp getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private ItemOp(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
              CK.Item.Builder subBuilder = null;
              if (((bitField0_ & 0x00000001) == 0x00000001)) {
                subBuilder = item_.toBuilder();
              }
              item_ = input.readMessage(CK.Item.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(item_);
                item_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000001;
              break;
            }
            case 18: {
              CK.Op.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = op_.toBuilder();
              }
              op_ = input.readMessage(CK.Op.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(op_);
                op_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_ItemOp_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_ItemOp_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.ItemOp.class, CK.ItemOp.Builder.class);
    }

    public static com.google.protobuf.Parser<ItemOp> PARSER =
        new com.google.protobuf.AbstractParser<ItemOp>() {
      public ItemOp parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new ItemOp(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<ItemOp> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int ITEM_FIELD_NUMBER = 1;
    private CK.Item item_;
    /**
     * <code>optional .Item item = 1;</code>
     */
    public boolean hasItem() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional .Item item = 1;</code>
     */
    public CK.Item getItem() {
      return item_;
    }
    /**
     * <code>optional .Item item = 1;</code>
     */
    public CK.ItemOrBuilder getItemOrBuilder() {
      return item_;
    }

    public static final int OP_FIELD_NUMBER = 2;
    private CK.Op op_;
    /**
     * <code>optional .Op op = 2;</code>
     */
    public boolean hasOp() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional .Op op = 2;</code>
     */
    public CK.Op getOp() {
      return op_;
    }
    /**
     * <code>optional .Op op = 2;</code>
     */
    public CK.OpOrBuilder getOpOrBuilder() {
      return op_;
    }

    private void initFields() {
      item_ = CK.Item.getDefaultInstance();
      op_ = CK.Op.getDefaultInstance();
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeMessage(1, item_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(2, op_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, item_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, op_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.ItemOp parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.ItemOp parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.ItemOp parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.ItemOp parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.ItemOp parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.ItemOp parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.ItemOp parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.ItemOp parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.ItemOp parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.ItemOp parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.ItemOp prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code ItemOp}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:ItemOp)
        CK.ItemOpOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_ItemOp_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_ItemOp_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.ItemOp.class, CK.ItemOp.Builder.class);
      }

      // Construct using CloudKit.ItemOp.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getItemFieldBuilder();
          getOpFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (itemBuilder_ == null) {
          item_ = CK.Item.getDefaultInstance();
        } else {
          itemBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        if (opBuilder_ == null) {
          op_ = CK.Op.getDefaultInstance();
        } else {
          opBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_ItemOp_descriptor;
      }

      public CK.ItemOp getDefaultInstanceForType() {
        return CK.ItemOp.getDefaultInstance();
      }

      public CK.ItemOp build() {
        CK.ItemOp result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.ItemOp buildPartial() {
        CK.ItemOp result = new CK.ItemOp(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (itemBuilder_ == null) {
          result.item_ = item_;
        } else {
          result.item_ = itemBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (opBuilder_ == null) {
          result.op_ = op_;
        } else {
          result.op_ = opBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.ItemOp) {
          return mergeFrom((CK.ItemOp)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.ItemOp other) {
        if (other == CK.ItemOp.getDefaultInstance()) return this;
        if (other.hasItem()) {
          mergeItem(other.getItem());
        }
        if (other.hasOp()) {
          mergeOp(other.getOp());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.ItemOp parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.ItemOp) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private CK.Item item_ = CK.Item.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Item, CK.Item.Builder, CK.ItemOrBuilder> itemBuilder_;
      /**
       * <code>optional .Item item = 1;</code>
       */
      public boolean hasItem() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional .Item item = 1;</code>
       */
      public CK.Item getItem() {
        if (itemBuilder_ == null) {
          return item_;
        } else {
          return itemBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .Item item = 1;</code>
       */
      public Builder setItem(CK.Item value) {
        if (itemBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          item_ = value;
          onChanged();
        } else {
          itemBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .Item item = 1;</code>
       */
      public Builder setItem(
          CK.Item.Builder builderForValue) {
        if (itemBuilder_ == null) {
          item_ = builderForValue.build();
          onChanged();
        } else {
          itemBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .Item item = 1;</code>
       */
      public Builder mergeItem(CK.Item value) {
        if (itemBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              item_ != CK.Item.getDefaultInstance()) {
            item_ =
              CK.Item.newBuilder(item_).mergeFrom(value).buildPartial();
          } else {
            item_ = value;
          }
          onChanged();
        } else {
          itemBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .Item item = 1;</code>
       */
      public Builder clearItem() {
        if (itemBuilder_ == null) {
          item_ = CK.Item.getDefaultInstance();
          onChanged();
        } else {
          itemBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      /**
       * <code>optional .Item item = 1;</code>
       */
      public CK.Item.Builder getItemBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getItemFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Item item = 1;</code>
       */
      public CK.ItemOrBuilder getItemOrBuilder() {
        if (itemBuilder_ != null) {
          return itemBuilder_.getMessageOrBuilder();
        } else {
          return item_;
        }
      }
      /**
       * <code>optional .Item item = 1;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Item, CK.Item.Builder, CK.ItemOrBuilder> 
          getItemFieldBuilder() {
        if (itemBuilder_ == null) {
          itemBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Item, CK.Item.Builder, CK.ItemOrBuilder>(
                  getItem(),
                  getParentForChildren(),
                  isClean());
          item_ = null;
        }
        return itemBuilder_;
      }

      private CK.Op op_ = CK.Op.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Op, CK.Op.Builder, CK.OpOrBuilder> opBuilder_;
      /**
       * <code>optional .Op op = 2;</code>
       */
      public boolean hasOp() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional .Op op = 2;</code>
       */
      public CK.Op getOp() {
        if (opBuilder_ == null) {
          return op_;
        } else {
          return opBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .Op op = 2;</code>
       */
      public Builder setOp(CK.Op value) {
        if (opBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          op_ = value;
          onChanged();
        } else {
          opBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Op op = 2;</code>
       */
      public Builder setOp(
          CK.Op.Builder builderForValue) {
        if (opBuilder_ == null) {
          op_ = builderForValue.build();
          onChanged();
        } else {
          opBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Op op = 2;</code>
       */
      public Builder mergeOp(CK.Op value) {
        if (opBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              op_ != CK.Op.getDefaultInstance()) {
            op_ =
              CK.Op.newBuilder(op_).mergeFrom(value).buildPartial();
          } else {
            op_ = value;
          }
          onChanged();
        } else {
          opBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Op op = 2;</code>
       */
      public Builder clearOp() {
        if (opBuilder_ == null) {
          op_ = CK.Op.getDefaultInstance();
          onChanged();
        } else {
          opBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      /**
       * <code>optional .Op op = 2;</code>
       */
      public CK.Op.Builder getOpBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getOpFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Op op = 2;</code>
       */
      public CK.OpOrBuilder getOpOrBuilder() {
        if (opBuilder_ != null) {
          return opBuilder_.getMessageOrBuilder();
        } else {
          return op_;
        }
      }
      /**
       * <code>optional .Op op = 2;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Op, CK.Op.Builder, CK.OpOrBuilder> 
          getOpFieldBuilder() {
        if (opBuilder_ == null) {
          opBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Op, CK.Op.Builder, CK.OpOrBuilder>(
                  getOp(),
                  getParentForChildren(),
                  isClean());
          op_ = null;
        }
        return opBuilder_;
      }

      // @@protoc_insertion_point(builder_scope:ItemOp)
    }

    static {
      defaultInstance = new ItemOp(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:ItemOp)
  }

  public interface OpResultOrBuilder extends
      // @@protoc_insertion_point(interface_extends:OpResult)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional .Op op = 1;</code>
     */
    boolean hasOp();
    /**
     * <code>optional .Op op = 1;</code>
     */
    CK.Op getOp();
    /**
     * <code>optional .Op op = 1;</code>
     */
    CK.OpOrBuilder getOpOrBuilder();

    /**
     * <code>optional .BytesString bytesString = 3;</code>
     */
    boolean hasBytesString();
    /**
     * <code>optional .BytesString bytesString = 3;</code>
     */
    CK.BytesString getBytesString();
    /**
     * <code>optional .BytesString bytesString = 3;</code>
     */
    CK.BytesStringOrBuilder getBytesStringOrBuilder();
  }
  /**
   * Protobuf type {@code OpResult}
   */
  public static final class OpResult extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:OpResult)
      OpResultOrBuilder {
    // Use OpResult.newBuilder() to construct.
    private OpResult(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private OpResult(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final OpResult defaultInstance;
    public static OpResult getDefaultInstance() {
      return defaultInstance;
    }

    public OpResult getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private OpResult(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
              CK.Op.Builder subBuilder = null;
              if (((bitField0_ & 0x00000001) == 0x00000001)) {
                subBuilder = op_.toBuilder();
              }
              op_ = input.readMessage(CK.Op.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(op_);
                op_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000001;
              break;
            }
            case 26: {
              CK.BytesString.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = bytesString_.toBuilder();
              }
              bytesString_ = input.readMessage(CK.BytesString.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(bytesString_);
                bytesString_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_OpResult_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_OpResult_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.OpResult.class, CK.OpResult.Builder.class);
    }

    public static com.google.protobuf.Parser<OpResult> PARSER =
        new com.google.protobuf.AbstractParser<OpResult>() {
      public OpResult parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new OpResult(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<OpResult> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int OP_FIELD_NUMBER = 1;
    private CK.Op op_;
    /**
     * <code>optional .Op op = 1;</code>
     */
    public boolean hasOp() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional .Op op = 1;</code>
     */
    public CK.Op getOp() {
      return op_;
    }
    /**
     * <code>optional .Op op = 1;</code>
     */
    public CK.OpOrBuilder getOpOrBuilder() {
      return op_;
    }

    public static final int BYTESSTRING_FIELD_NUMBER = 3;
    private CK.BytesString bytesString_;
    /**
     * <code>optional .BytesString bytesString = 3;</code>
     */
    public boolean hasBytesString() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional .BytesString bytesString = 3;</code>
     */
    public CK.BytesString getBytesString() {
      return bytesString_;
    }
    /**
     * <code>optional .BytesString bytesString = 3;</code>
     */
    public CK.BytesStringOrBuilder getBytesStringOrBuilder() {
      return bytesString_;
    }

    private void initFields() {
      op_ = CK.Op.getDefaultInstance();
      bytesString_ = CK.BytesString.getDefaultInstance();
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeMessage(1, op_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(3, bytesString_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, op_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, bytesString_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.OpResult parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.OpResult parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.OpResult parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.OpResult parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.OpResult parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.OpResult parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.OpResult parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.OpResult parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.OpResult parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.OpResult parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.OpResult prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code OpResult}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:OpResult)
        CK.OpResultOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_OpResult_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_OpResult_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.OpResult.class, CK.OpResult.Builder.class);
      }

      // Construct using CloudKit.OpResult.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getOpFieldBuilder();
          getBytesStringFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (opBuilder_ == null) {
          op_ = CK.Op.getDefaultInstance();
        } else {
          opBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        if (bytesStringBuilder_ == null) {
          bytesString_ = CK.BytesString.getDefaultInstance();
        } else {
          bytesStringBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_OpResult_descriptor;
      }

      public CK.OpResult getDefaultInstanceForType() {
        return CK.OpResult.getDefaultInstance();
      }

      public CK.OpResult build() {
        CK.OpResult result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.OpResult buildPartial() {
        CK.OpResult result = new CK.OpResult(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (opBuilder_ == null) {
          result.op_ = op_;
        } else {
          result.op_ = opBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (bytesStringBuilder_ == null) {
          result.bytesString_ = bytesString_;
        } else {
          result.bytesString_ = bytesStringBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.OpResult) {
          return mergeFrom((CK.OpResult)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.OpResult other) {
        if (other == CK.OpResult.getDefaultInstance()) return this;
        if (other.hasOp()) {
          mergeOp(other.getOp());
        }
        if (other.hasBytesString()) {
          mergeBytesString(other.getBytesString());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.OpResult parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.OpResult) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private CK.Op op_ = CK.Op.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Op, CK.Op.Builder, CK.OpOrBuilder> opBuilder_;
      /**
       * <code>optional .Op op = 1;</code>
       */
      public boolean hasOp() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional .Op op = 1;</code>
       */
      public CK.Op getOp() {
        if (opBuilder_ == null) {
          return op_;
        } else {
          return opBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .Op op = 1;</code>
       */
      public Builder setOp(CK.Op value) {
        if (opBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          op_ = value;
          onChanged();
        } else {
          opBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .Op op = 1;</code>
       */
      public Builder setOp(
          CK.Op.Builder builderForValue) {
        if (opBuilder_ == null) {
          op_ = builderForValue.build();
          onChanged();
        } else {
          opBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .Op op = 1;</code>
       */
      public Builder mergeOp(CK.Op value) {
        if (opBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              op_ != CK.Op.getDefaultInstance()) {
            op_ =
              CK.Op.newBuilder(op_).mergeFrom(value).buildPartial();
          } else {
            op_ = value;
          }
          onChanged();
        } else {
          opBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .Op op = 1;</code>
       */
      public Builder clearOp() {
        if (opBuilder_ == null) {
          op_ = CK.Op.getDefaultInstance();
          onChanged();
        } else {
          opBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      /**
       * <code>optional .Op op = 1;</code>
       */
      public CK.Op.Builder getOpBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getOpFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Op op = 1;</code>
       */
      public CK.OpOrBuilder getOpOrBuilder() {
        if (opBuilder_ != null) {
          return opBuilder_.getMessageOrBuilder();
        } else {
          return op_;
        }
      }
      /**
       * <code>optional .Op op = 1;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Op, CK.Op.Builder, CK.OpOrBuilder> 
          getOpFieldBuilder() {
        if (opBuilder_ == null) {
          opBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Op, CK.Op.Builder, CK.OpOrBuilder>(
                  getOp(),
                  getParentForChildren(),
                  isClean());
          op_ = null;
        }
        return opBuilder_;
      }

      private CK.BytesString bytesString_ = CK.BytesString.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.BytesString, CK.BytesString.Builder, CK.BytesStringOrBuilder> bytesStringBuilder_;
      /**
       * <code>optional .BytesString bytesString = 3;</code>
       */
      public boolean hasBytesString() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional .BytesString bytesString = 3;</code>
       */
      public CK.BytesString getBytesString() {
        if (bytesStringBuilder_ == null) {
          return bytesString_;
        } else {
          return bytesStringBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .BytesString bytesString = 3;</code>
       */
      public Builder setBytesString(CK.BytesString value) {
        if (bytesStringBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          bytesString_ = value;
          onChanged();
        } else {
          bytesStringBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .BytesString bytesString = 3;</code>
       */
      public Builder setBytesString(
          CK.BytesString.Builder builderForValue) {
        if (bytesStringBuilder_ == null) {
          bytesString_ = builderForValue.build();
          onChanged();
        } else {
          bytesStringBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .BytesString bytesString = 3;</code>
       */
      public Builder mergeBytesString(CK.BytesString value) {
        if (bytesStringBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              bytesString_ != CK.BytesString.getDefaultInstance()) {
            bytesString_ =
              CK.BytesString.newBuilder(bytesString_).mergeFrom(value).buildPartial();
          } else {
            bytesString_ = value;
          }
          onChanged();
        } else {
          bytesStringBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .BytesString bytesString = 3;</code>
       */
      public Builder clearBytesString() {
        if (bytesStringBuilder_ == null) {
          bytesString_ = CK.BytesString.getDefaultInstance();
          onChanged();
        } else {
          bytesStringBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      /**
       * <code>optional .BytesString bytesString = 3;</code>
       */
      public CK.BytesString.Builder getBytesStringBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getBytesStringFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .BytesString bytesString = 3;</code>
       */
      public CK.BytesStringOrBuilder getBytesStringOrBuilder() {
        if (bytesStringBuilder_ != null) {
          return bytesStringBuilder_.getMessageOrBuilder();
        } else {
          return bytesString_;
        }
      }
      /**
       * <code>optional .BytesString bytesString = 3;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.BytesString, CK.BytesString.Builder, CK.BytesStringOrBuilder> 
          getBytesStringFieldBuilder() {
        if (bytesStringBuilder_ == null) {
          bytesStringBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.BytesString, CK.BytesString.Builder, CK.BytesStringOrBuilder>(
                  getBytesString(),
                  getParentForChildren(),
                  isClean());
          bytesString_ = null;
        }
        return bytesStringBuilder_;
      }

      // @@protoc_insertion_point(builder_scope:OpResult)
    }

    static {
      defaultInstance = new OpResult(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:OpResult)
  }

  public interface ItemOrBuilder extends
      // @@protoc_insertion_point(interface_extends:Item)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string value = 1;</code>
     */
    boolean hasValue();
    /**
     * <code>optional string value = 1;</code>
     */
    java.lang.String getValue();
    /**
     * <code>optional string value = 1;</code>
     */
    com.google.protobuf.ByteString
        getValueBytes();

    /**
     * <code>optional uint32 type = 2;</code>
     */
    boolean hasType();
    /**
     * <code>optional uint32 type = 2;</code>
     */
    int getType();
  }
  /**
   * Protobuf type {@code Item}
   *
   * <pre>
   * Record_Response_211_1_7_2_11_9_2_1 Record_Response_211_1_7_2_11_9_2_2_1
   * </pre>
   */
  public static final class Item extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:Item)
      ItemOrBuilder {
    // Use Item.newBuilder() to construct.
    private Item(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Item(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Item defaultInstance;
    public static Item getDefaultInstance() {
      return defaultInstance;
    }

    public Item getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Item(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
              value_ = bs;
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              type_ = input.readUInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_Item_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_Item_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.Item.class, CK.Item.Builder.class);
    }

    public static com.google.protobuf.Parser<Item> PARSER =
        new com.google.protobuf.AbstractParser<Item>() {
      public Item parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Item(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Item> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int VALUE_FIELD_NUMBER = 1;
    private java.lang.Object value_;
    /**
     * <code>optional string value = 1;</code>
     */
    public boolean hasValue() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional string value = 1;</code>
     */
    public java.lang.String getValue() {
      java.lang.Object ref = value_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          value_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string value = 1;</code>
     */
    public com.google.protobuf.ByteString
        getValueBytes() {
      java.lang.Object ref = value_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        value_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int TYPE_FIELD_NUMBER = 2;
    private int type_;
    /**
     * <code>optional uint32 type = 2;</code>
     */
    public boolean hasType() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional uint32 type = 2;</code>
     */
    public int getType() {
      return type_;
    }

    private void initFields() {
      value_ = "";
      type_ = 0;
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getValueBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeUInt32(2, type_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getValueBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(2, type_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.Item parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Item parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Item parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Item parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Item parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Item parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.Item parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.Item parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.Item parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Item parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.Item prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code Item}
     *
     * <pre>
     * Record_Response_211_1_7_2_11_9_2_1 Record_Response_211_1_7_2_11_9_2_2_1
     * </pre>
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:Item)
        CK.ItemOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_Item_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_Item_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.Item.class, CK.Item.Builder.class);
      }

      // Construct using CloudKit.Item.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        value_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        type_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_Item_descriptor;
      }

      public CK.Item getDefaultInstanceForType() {
        return CK.Item.getDefaultInstance();
      }

      public CK.Item build() {
        CK.Item result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.Item buildPartial() {
        CK.Item result = new CK.Item(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.value_ = value_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.type_ = type_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.Item) {
          return mergeFrom((CK.Item)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.Item other) {
        if (other == CK.Item.getDefaultInstance()) return this;
        if (other.hasValue()) {
          bitField0_ |= 0x00000001;
          value_ = other.value_;
          onChanged();
        }
        if (other.hasType()) {
          setType(other.getType());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.Item parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.Item) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object value_ = "";
      /**
       * <code>optional string value = 1;</code>
       */
      public boolean hasValue() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional string value = 1;</code>
       */
      public java.lang.String getValue() {
        java.lang.Object ref = value_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            value_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string value = 1;</code>
       */
      public com.google.protobuf.ByteString
          getValueBytes() {
        java.lang.Object ref = value_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          value_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string value = 1;</code>
       */
      public Builder setValue(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        value_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string value = 1;</code>
       */
      public Builder clearValue() {
        bitField0_ = (bitField0_ & ~0x00000001);
        value_ = getDefaultInstance().getValue();
        onChanged();
        return this;
      }
      /**
       * <code>optional string value = 1;</code>
       */
      public Builder setValueBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        value_ = value;
        onChanged();
        return this;
      }

      private int type_ ;
      /**
       * <code>optional uint32 type = 2;</code>
       */
      public boolean hasType() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional uint32 type = 2;</code>
       */
      public int getType() {
        return type_;
      }
      /**
       * <code>optional uint32 type = 2;</code>
       */
      public Builder setType(int value) {
        bitField0_ |= 0x00000002;
        type_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 type = 2;</code>
       */
      public Builder clearType() {
        bitField0_ = (bitField0_ & ~0x00000002);
        type_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:Item)
    }

    static {
      defaultInstance = new Item(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:Item)
  }

  public interface OpOrBuilder extends
      // @@protoc_insertion_point(interface_extends:Op)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional .Item item = 1;</code>
     */
    boolean hasItem();
    /**
     * <code>optional .Item item = 1;</code>
     */
    CK.Item getItem();
    /**
     * <code>optional .Item item = 1;</code>
     */
    CK.ItemOrBuilder getItemOrBuilder();

    /**
     * <code>optional .Item ckUserID = 2;</code>
     */
    boolean hasCkUserID();
    /**
     * <code>optional .Item ckUserID = 2;</code>
     */
    CK.Item getCkUserID();
    /**
     * <code>optional .Item ckUserID = 2;</code>
     */
    CK.ItemOrBuilder getCkUserIDOrBuilder();
  }
  /**
   * Protobuf type {@code Op}
   */
  public static final class Op extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:Op)
      OpOrBuilder {
    // Use Op.newBuilder() to construct.
    private Op(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Op(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Op defaultInstance;
    public static Op getDefaultInstance() {
      return defaultInstance;
    }

    public Op getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Op(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
              CK.Item.Builder subBuilder = null;
              if (((bitField0_ & 0x00000001) == 0x00000001)) {
                subBuilder = item_.toBuilder();
              }
              item_ = input.readMessage(CK.Item.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(item_);
                item_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000001;
              break;
            }
            case 18: {
              CK.Item.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = ckUserID_.toBuilder();
              }
              ckUserID_ = input.readMessage(CK.Item.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(ckUserID_);
                ckUserID_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_Op_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_Op_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.Op.class, CK.Op.Builder.class);
    }

    public static com.google.protobuf.Parser<Op> PARSER =
        new com.google.protobuf.AbstractParser<Op>() {
      public Op parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Op(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Op> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int ITEM_FIELD_NUMBER = 1;
    private CK.Item item_;
    /**
     * <code>optional .Item item = 1;</code>
     */
    public boolean hasItem() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional .Item item = 1;</code>
     */
    public CK.Item getItem() {
      return item_;
    }
    /**
     * <code>optional .Item item = 1;</code>
     */
    public CK.ItemOrBuilder getItemOrBuilder() {
      return item_;
    }

    public static final int CKUSERID_FIELD_NUMBER = 2;
    private CK.Item ckUserID_;
    /**
     * <code>optional .Item ckUserID = 2;</code>
     */
    public boolean hasCkUserID() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional .Item ckUserID = 2;</code>
     */
    public CK.Item getCkUserID() {
      return ckUserID_;
    }
    /**
     * <code>optional .Item ckUserID = 2;</code>
     */
    public CK.ItemOrBuilder getCkUserIDOrBuilder() {
      return ckUserID_;
    }

    private void initFields() {
      item_ = CK.Item.getDefaultInstance();
      ckUserID_ = CK.Item.getDefaultInstance();
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeMessage(1, item_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(2, ckUserID_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, item_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, ckUserID_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.Op parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Op parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Op parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Op parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Op parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Op parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.Op parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.Op parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.Op parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Op parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.Op prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code Op}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:Op)
        CK.OpOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_Op_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_Op_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.Op.class, CK.Op.Builder.class);
      }

      // Construct using CloudKit.Op.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getItemFieldBuilder();
          getCkUserIDFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (itemBuilder_ == null) {
          item_ = CK.Item.getDefaultInstance();
        } else {
          itemBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        if (ckUserIDBuilder_ == null) {
          ckUserID_ = CK.Item.getDefaultInstance();
        } else {
          ckUserIDBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_Op_descriptor;
      }

      public CK.Op getDefaultInstanceForType() {
        return CK.Op.getDefaultInstance();
      }

      public CK.Op build() {
        CK.Op result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.Op buildPartial() {
        CK.Op result = new CK.Op(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (itemBuilder_ == null) {
          result.item_ = item_;
        } else {
          result.item_ = itemBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (ckUserIDBuilder_ == null) {
          result.ckUserID_ = ckUserID_;
        } else {
          result.ckUserID_ = ckUserIDBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.Op) {
          return mergeFrom((CK.Op)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.Op other) {
        if (other == CK.Op.getDefaultInstance()) return this;
        if (other.hasItem()) {
          mergeItem(other.getItem());
        }
        if (other.hasCkUserID()) {
          mergeCkUserID(other.getCkUserID());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.Op parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.Op) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private CK.Item item_ = CK.Item.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Item, CK.Item.Builder, CK.ItemOrBuilder> itemBuilder_;
      /**
       * <code>optional .Item item = 1;</code>
       */
      public boolean hasItem() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional .Item item = 1;</code>
       */
      public CK.Item getItem() {
        if (itemBuilder_ == null) {
          return item_;
        } else {
          return itemBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .Item item = 1;</code>
       */
      public Builder setItem(CK.Item value) {
        if (itemBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          item_ = value;
          onChanged();
        } else {
          itemBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .Item item = 1;</code>
       */
      public Builder setItem(
          CK.Item.Builder builderForValue) {
        if (itemBuilder_ == null) {
          item_ = builderForValue.build();
          onChanged();
        } else {
          itemBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .Item item = 1;</code>
       */
      public Builder mergeItem(CK.Item value) {
        if (itemBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              item_ != CK.Item.getDefaultInstance()) {
            item_ =
              CK.Item.newBuilder(item_).mergeFrom(value).buildPartial();
          } else {
            item_ = value;
          }
          onChanged();
        } else {
          itemBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .Item item = 1;</code>
       */
      public Builder clearItem() {
        if (itemBuilder_ == null) {
          item_ = CK.Item.getDefaultInstance();
          onChanged();
        } else {
          itemBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      /**
       * <code>optional .Item item = 1;</code>
       */
      public CK.Item.Builder getItemBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getItemFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Item item = 1;</code>
       */
      public CK.ItemOrBuilder getItemOrBuilder() {
        if (itemBuilder_ != null) {
          return itemBuilder_.getMessageOrBuilder();
        } else {
          return item_;
        }
      }
      /**
       * <code>optional .Item item = 1;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Item, CK.Item.Builder, CK.ItemOrBuilder> 
          getItemFieldBuilder() {
        if (itemBuilder_ == null) {
          itemBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Item, CK.Item.Builder, CK.ItemOrBuilder>(
                  getItem(),
                  getParentForChildren(),
                  isClean());
          item_ = null;
        }
        return itemBuilder_;
      }

      private CK.Item ckUserID_ = CK.Item.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Item, CK.Item.Builder, CK.ItemOrBuilder> ckUserIDBuilder_;
      /**
       * <code>optional .Item ckUserID = 2;</code>
       */
      public boolean hasCkUserID() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional .Item ckUserID = 2;</code>
       */
      public CK.Item getCkUserID() {
        if (ckUserIDBuilder_ == null) {
          return ckUserID_;
        } else {
          return ckUserIDBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .Item ckUserID = 2;</code>
       */
      public Builder setCkUserID(CK.Item value) {
        if (ckUserIDBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ckUserID_ = value;
          onChanged();
        } else {
          ckUserIDBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Item ckUserID = 2;</code>
       */
      public Builder setCkUserID(
          CK.Item.Builder builderForValue) {
        if (ckUserIDBuilder_ == null) {
          ckUserID_ = builderForValue.build();
          onChanged();
        } else {
          ckUserIDBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Item ckUserID = 2;</code>
       */
      public Builder mergeCkUserID(CK.Item value) {
        if (ckUserIDBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              ckUserID_ != CK.Item.getDefaultInstance()) {
            ckUserID_ =
              CK.Item.newBuilder(ckUserID_).mergeFrom(value).buildPartial();
          } else {
            ckUserID_ = value;
          }
          onChanged();
        } else {
          ckUserIDBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Item ckUserID = 2;</code>
       */
      public Builder clearCkUserID() {
        if (ckUserIDBuilder_ == null) {
          ckUserID_ = CK.Item.getDefaultInstance();
          onChanged();
        } else {
          ckUserIDBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      /**
       * <code>optional .Item ckUserID = 2;</code>
       */
      public CK.Item.Builder getCkUserIDBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getCkUserIDFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Item ckUserID = 2;</code>
       */
      public CK.ItemOrBuilder getCkUserIDOrBuilder() {
        if (ckUserIDBuilder_ != null) {
          return ckUserIDBuilder_.getMessageOrBuilder();
        } else {
          return ckUserID_;
        }
      }
      /**
       * <code>optional .Item ckUserID = 2;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Item, CK.Item.Builder, CK.ItemOrBuilder> 
          getCkUserIDFieldBuilder() {
        if (ckUserIDBuilder_ == null) {
          ckUserIDBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Item, CK.Item.Builder, CK.ItemOrBuilder>(
                  getCkUserID(),
                  getParentForChildren(),
                  isClean());
          ckUserID_ = null;
        }
        return ckUserIDBuilder_;
      }

      // @@protoc_insertion_point(builder_scope:Op)
    }

    static {
      defaultInstance = new Op(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:Op)
  }

  public interface BytesStringOrBuilder extends
      // @@protoc_insertion_point(interface_extends:BytesString)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional bytes bytes = 1;</code>
     */
    boolean hasBytes();
    /**
     * <code>optional bytes bytes = 1;</code>
     */
    com.google.protobuf.ByteString getBytes();

    /**
     * <code>optional string string = 2;</code>
     */
    boolean hasString();
    /**
     * <code>optional string string = 2;</code>
     */
    java.lang.String getString();
    /**
     * <code>optional string string = 2;</code>
     */
    com.google.protobuf.ByteString
        getStringBytes();
  }
  /**
   * Protobuf type {@code BytesString}
   */
  public static final class BytesString extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:BytesString)
      BytesStringOrBuilder {
    // Use BytesString.newBuilder() to construct.
    private BytesString(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private BytesString(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final BytesString defaultInstance;
    public static BytesString getDefaultInstance() {
      return defaultInstance;
    }

    public BytesString getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private BytesString(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
              bitField0_ |= 0x00000001;
              bytes_ = input.readBytes();
              break;
            }
            case 18: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000002;
              string_ = bs;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_BytesString_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_BytesString_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.BytesString.class, CK.BytesString.Builder.class);
    }

    public static com.google.protobuf.Parser<BytesString> PARSER =
        new com.google.protobuf.AbstractParser<BytesString>() {
      public BytesString parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new BytesString(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<BytesString> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int BYTES_FIELD_NUMBER = 1;
    private com.google.protobuf.ByteString bytes_;
    /**
     * <code>optional bytes bytes = 1;</code>
     */
    public boolean hasBytes() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional bytes bytes = 1;</code>
     */
    public com.google.protobuf.ByteString getBytes() {
      return bytes_;
    }

    public static final int STRING_FIELD_NUMBER = 2;
    private java.lang.Object string_;
    /**
     * <code>optional string string = 2;</code>
     */
    public boolean hasString() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional string string = 2;</code>
     */
    public java.lang.String getString() {
      java.lang.Object ref = string_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          string_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string string = 2;</code>
     */
    public com.google.protobuf.ByteString
        getStringBytes() {
      java.lang.Object ref = string_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        string_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private void initFields() {
      bytes_ = com.google.protobuf.ByteString.EMPTY;
      string_ = "";
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, bytes_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getStringBytes());
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, bytes_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getStringBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.BytesString parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.BytesString parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.BytesString parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.BytesString parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.BytesString parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.BytesString parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.BytesString parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.BytesString parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.BytesString parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.BytesString parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.BytesString prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code BytesString}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:BytesString)
        CK.BytesStringOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_BytesString_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_BytesString_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.BytesString.class, CK.BytesString.Builder.class);
      }

      // Construct using CloudKit.BytesString.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        bytes_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        string_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_BytesString_descriptor;
      }

      public CK.BytesString getDefaultInstanceForType() {
        return CK.BytesString.getDefaultInstance();
      }

      public CK.BytesString build() {
        CK.BytesString result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.BytesString buildPartial() {
        CK.BytesString result = new CK.BytesString(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.bytes_ = bytes_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.string_ = string_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.BytesString) {
          return mergeFrom((CK.BytesString)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.BytesString other) {
        if (other == CK.BytesString.getDefaultInstance()) return this;
        if (other.hasBytes()) {
          setBytes(other.getBytes());
        }
        if (other.hasString()) {
          bitField0_ |= 0x00000002;
          string_ = other.string_;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.BytesString parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.BytesString) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.google.protobuf.ByteString bytes_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes bytes = 1;</code>
       */
      public boolean hasBytes() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional bytes bytes = 1;</code>
       */
      public com.google.protobuf.ByteString getBytes() {
        return bytes_;
      }
      /**
       * <code>optional bytes bytes = 1;</code>
       */
      public Builder setBytes(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        bytes_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes bytes = 1;</code>
       */
      public Builder clearBytes() {
        bitField0_ = (bitField0_ & ~0x00000001);
        bytes_ = getDefaultInstance().getBytes();
        onChanged();
        return this;
      }

      private java.lang.Object string_ = "";
      /**
       * <code>optional string string = 2;</code>
       */
      public boolean hasString() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional string string = 2;</code>
       */
      public java.lang.String getString() {
        java.lang.Object ref = string_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            string_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string string = 2;</code>
       */
      public com.google.protobuf.ByteString
          getStringBytes() {
        java.lang.Object ref = string_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          string_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string string = 2;</code>
       */
      public Builder setString(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        string_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string string = 2;</code>
       */
      public Builder clearString() {
        bitField0_ = (bitField0_ & ~0x00000002);
        string_ = getDefaultInstance().getString();
        onChanged();
        return this;
      }
      /**
       * <code>optional string string = 2;</code>
       */
      public Builder setStringBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        string_ = value;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:BytesString)
    }

    static {
      defaultInstance = new BytesString(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:BytesString)
  }

  public interface Fixed64PairOrBuilder extends
      // @@protoc_insertion_point(interface_extends:Fixed64Pair)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional .Fixed64 one = 1;</code>
     */
    boolean hasOne();
    /**
     * <code>optional .Fixed64 one = 1;</code>
     */
    CK.Fixed64 getOne();
    /**
     * <code>optional .Fixed64 one = 1;</code>
     */
    CK.Fixed64OrBuilder getOneOrBuilder();

    /**
     * <code>optional .Fixed64 two = 2;</code>
     */
    boolean hasTwo();
    /**
     * <code>optional .Fixed64 two = 2;</code>
     */
    CK.Fixed64 getTwo();
    /**
     * <code>optional .Fixed64 two = 2;</code>
     */
    CK.Fixed64OrBuilder getTwoOrBuilder();
  }
  /**
   * Protobuf type {@code Fixed64Pair}
   */
  public static final class Fixed64Pair extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:Fixed64Pair)
      Fixed64PairOrBuilder {
    // Use Fixed64Pair.newBuilder() to construct.
    private Fixed64Pair(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Fixed64Pair(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Fixed64Pair defaultInstance;
    public static Fixed64Pair getDefaultInstance() {
      return defaultInstance;
    }

    public Fixed64Pair getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Fixed64Pair(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
              CK.Fixed64.Builder subBuilder = null;
              if (((bitField0_ & 0x00000001) == 0x00000001)) {
                subBuilder = one_.toBuilder();
              }
              one_ = input.readMessage(CK.Fixed64.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(one_);
                one_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000001;
              break;
            }
            case 18: {
              CK.Fixed64.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = two_.toBuilder();
              }
              two_ = input.readMessage(CK.Fixed64.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(two_);
                two_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_Fixed64Pair_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_Fixed64Pair_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.Fixed64Pair.class, CK.Fixed64Pair.Builder.class);
    }

    public static com.google.protobuf.Parser<Fixed64Pair> PARSER =
        new com.google.protobuf.AbstractParser<Fixed64Pair>() {
      public Fixed64Pair parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Fixed64Pair(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Fixed64Pair> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int ONE_FIELD_NUMBER = 1;
    private CK.Fixed64 one_;
    /**
     * <code>optional .Fixed64 one = 1;</code>
     */
    public boolean hasOne() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional .Fixed64 one = 1;</code>
     */
    public CK.Fixed64 getOne() {
      return one_;
    }
    /**
     * <code>optional .Fixed64 one = 1;</code>
     */
    public CK.Fixed64OrBuilder getOneOrBuilder() {
      return one_;
    }

    public static final int TWO_FIELD_NUMBER = 2;
    private CK.Fixed64 two_;
    /**
     * <code>optional .Fixed64 two = 2;</code>
     */
    public boolean hasTwo() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional .Fixed64 two = 2;</code>
     */
    public CK.Fixed64 getTwo() {
      return two_;
    }
    /**
     * <code>optional .Fixed64 two = 2;</code>
     */
    public CK.Fixed64OrBuilder getTwoOrBuilder() {
      return two_;
    }

    private void initFields() {
      one_ = CK.Fixed64.getDefaultInstance();
      two_ = CK.Fixed64.getDefaultInstance();
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeMessage(1, one_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(2, two_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, one_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, two_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.Fixed64Pair parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Fixed64Pair parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Fixed64Pair parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Fixed64Pair parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Fixed64Pair parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Fixed64Pair parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.Fixed64Pair parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.Fixed64Pair parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.Fixed64Pair parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Fixed64Pair parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.Fixed64Pair prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code Fixed64Pair}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:Fixed64Pair)
        CK.Fixed64PairOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_Fixed64Pair_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_Fixed64Pair_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.Fixed64Pair.class, CK.Fixed64Pair.Builder.class);
      }

      // Construct using CloudKit.Fixed64Pair.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getOneFieldBuilder();
          getTwoFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (oneBuilder_ == null) {
          one_ = CK.Fixed64.getDefaultInstance();
        } else {
          oneBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        if (twoBuilder_ == null) {
          two_ = CK.Fixed64.getDefaultInstance();
        } else {
          twoBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_Fixed64Pair_descriptor;
      }

      public CK.Fixed64Pair getDefaultInstanceForType() {
        return CK.Fixed64Pair.getDefaultInstance();
      }

      public CK.Fixed64Pair build() {
        CK.Fixed64Pair result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.Fixed64Pair buildPartial() {
        CK.Fixed64Pair result = new CK.Fixed64Pair(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (oneBuilder_ == null) {
          result.one_ = one_;
        } else {
          result.one_ = oneBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (twoBuilder_ == null) {
          result.two_ = two_;
        } else {
          result.two_ = twoBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.Fixed64Pair) {
          return mergeFrom((CK.Fixed64Pair)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.Fixed64Pair other) {
        if (other == CK.Fixed64Pair.getDefaultInstance()) return this;
        if (other.hasOne()) {
          mergeOne(other.getOne());
        }
        if (other.hasTwo()) {
          mergeTwo(other.getTwo());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.Fixed64Pair parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.Fixed64Pair) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private CK.Fixed64 one_ = CK.Fixed64.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Fixed64, CK.Fixed64.Builder, CK.Fixed64OrBuilder> oneBuilder_;
      /**
       * <code>optional .Fixed64 one = 1;</code>
       */
      public boolean hasOne() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional .Fixed64 one = 1;</code>
       */
      public CK.Fixed64 getOne() {
        if (oneBuilder_ == null) {
          return one_;
        } else {
          return oneBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .Fixed64 one = 1;</code>
       */
      public Builder setOne(CK.Fixed64 value) {
        if (oneBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          one_ = value;
          onChanged();
        } else {
          oneBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .Fixed64 one = 1;</code>
       */
      public Builder setOne(
          CK.Fixed64.Builder builderForValue) {
        if (oneBuilder_ == null) {
          one_ = builderForValue.build();
          onChanged();
        } else {
          oneBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .Fixed64 one = 1;</code>
       */
      public Builder mergeOne(CK.Fixed64 value) {
        if (oneBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              one_ != CK.Fixed64.getDefaultInstance()) {
            one_ =
              CK.Fixed64.newBuilder(one_).mergeFrom(value).buildPartial();
          } else {
            one_ = value;
          }
          onChanged();
        } else {
          oneBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>optional .Fixed64 one = 1;</code>
       */
      public Builder clearOne() {
        if (oneBuilder_ == null) {
          one_ = CK.Fixed64.getDefaultInstance();
          onChanged();
        } else {
          oneBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      /**
       * <code>optional .Fixed64 one = 1;</code>
       */
      public CK.Fixed64.Builder getOneBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getOneFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Fixed64 one = 1;</code>
       */
      public CK.Fixed64OrBuilder getOneOrBuilder() {
        if (oneBuilder_ != null) {
          return oneBuilder_.getMessageOrBuilder();
        } else {
          return one_;
        }
      }
      /**
       * <code>optional .Fixed64 one = 1;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Fixed64, CK.Fixed64.Builder, CK.Fixed64OrBuilder> 
          getOneFieldBuilder() {
        if (oneBuilder_ == null) {
          oneBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Fixed64, CK.Fixed64.Builder, CK.Fixed64OrBuilder>(
                  getOne(),
                  getParentForChildren(),
                  isClean());
          one_ = null;
        }
        return oneBuilder_;
      }

      private CK.Fixed64 two_ = CK.Fixed64.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          CK.Fixed64, CK.Fixed64.Builder, CK.Fixed64OrBuilder> twoBuilder_;
      /**
       * <code>optional .Fixed64 two = 2;</code>
       */
      public boolean hasTwo() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional .Fixed64 two = 2;</code>
       */
      public CK.Fixed64 getTwo() {
        if (twoBuilder_ == null) {
          return two_;
        } else {
          return twoBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .Fixed64 two = 2;</code>
       */
      public Builder setTwo(CK.Fixed64 value) {
        if (twoBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          two_ = value;
          onChanged();
        } else {
          twoBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Fixed64 two = 2;</code>
       */
      public Builder setTwo(
          CK.Fixed64.Builder builderForValue) {
        if (twoBuilder_ == null) {
          two_ = builderForValue.build();
          onChanged();
        } else {
          twoBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Fixed64 two = 2;</code>
       */
      public Builder mergeTwo(CK.Fixed64 value) {
        if (twoBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              two_ != CK.Fixed64.getDefaultInstance()) {
            two_ =
              CK.Fixed64.newBuilder(two_).mergeFrom(value).buildPartial();
          } else {
            two_ = value;
          }
          onChanged();
        } else {
          twoBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      /**
       * <code>optional .Fixed64 two = 2;</code>
       */
      public Builder clearTwo() {
        if (twoBuilder_ == null) {
          two_ = CK.Fixed64.getDefaultInstance();
          onChanged();
        } else {
          twoBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      /**
       * <code>optional .Fixed64 two = 2;</code>
       */
      public CK.Fixed64.Builder getTwoBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getTwoFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .Fixed64 two = 2;</code>
       */
      public CK.Fixed64OrBuilder getTwoOrBuilder() {
        if (twoBuilder_ != null) {
          return twoBuilder_.getMessageOrBuilder();
        } else {
          return two_;
        }
      }
      /**
       * <code>optional .Fixed64 two = 2;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          CK.Fixed64, CK.Fixed64.Builder, CK.Fixed64OrBuilder> 
          getTwoFieldBuilder() {
        if (twoBuilder_ == null) {
          twoBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              CK.Fixed64, CK.Fixed64.Builder, CK.Fixed64OrBuilder>(
                  getTwo(),
                  getParentForChildren(),
                  isClean());
          two_ = null;
        }
        return twoBuilder_;
      }

      // @@protoc_insertion_point(builder_scope:Fixed64Pair)
    }

    static {
      defaultInstance = new Fixed64Pair(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:Fixed64Pair)
  }

  public interface Fixed64OrBuilder extends
      // @@protoc_insertion_point(interface_extends:Fixed64)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional fixed64 value = 1;</code>
     */
    boolean hasValue();
    /**
     * <code>optional fixed64 value = 1;</code>
     */
    long getValue();
  }
  /**
   * Protobuf type {@code Fixed64}
   *
   * <pre>
   * Record_Response_211_1_7_2_11_6
   * </pre>
   */
  public static final class Fixed64 extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:Fixed64)
      Fixed64OrBuilder {
    // Use Fixed64.newBuilder() to construct.
    private Fixed64(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Fixed64(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Fixed64 defaultInstance;
    public static Fixed64 getDefaultInstance() {
      return defaultInstance;
    }

    public Fixed64 getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Fixed64(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
            case 9: {
              bitField0_ |= 0x00000001;
              value_ = input.readFixed64();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_Fixed64_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_Fixed64_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.Fixed64.class, CK.Fixed64.Builder.class);
    }

    public static com.google.protobuf.Parser<Fixed64> PARSER =
        new com.google.protobuf.AbstractParser<Fixed64>() {
      public Fixed64 parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Fixed64(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Fixed64> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int VALUE_FIELD_NUMBER = 1;
    private long value_;
    /**
     * <code>optional fixed64 value = 1;</code>
     */
    public boolean hasValue() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional fixed64 value = 1;</code>
     */
    public long getValue() {
      return value_;
    }

    private void initFields() {
      value_ = 0L;
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeFixed64(1, value_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeFixed64Size(1, value_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.Fixed64 parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Fixed64 parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Fixed64 parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.Fixed64 parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.Fixed64 parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Fixed64 parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.Fixed64 parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.Fixed64 parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.Fixed64 parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.Fixed64 parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.Fixed64 prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code Fixed64}
     *
     * <pre>
     * Record_Response_211_1_7_2_11_6
     * </pre>
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:Fixed64)
        CK.Fixed64OrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_Fixed64_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_Fixed64_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.Fixed64.class, CK.Fixed64.Builder.class);
      }

      // Construct using CloudKit.Fixed64.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        value_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_Fixed64_descriptor;
      }

      public CK.Fixed64 getDefaultInstanceForType() {
        return CK.Fixed64.getDefaultInstance();
      }

      public CK.Fixed64 build() {
        CK.Fixed64 result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.Fixed64 buildPartial() {
        CK.Fixed64 result = new CK.Fixed64(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.value_ = value_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.Fixed64) {
          return mergeFrom((CK.Fixed64)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.Fixed64 other) {
        if (other == CK.Fixed64.getDefaultInstance()) return this;
        if (other.hasValue()) {
          setValue(other.getValue());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.Fixed64 parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.Fixed64) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private long value_ ;
      /**
       * <code>optional fixed64 value = 1;</code>
       */
      public boolean hasValue() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional fixed64 value = 1;</code>
       */
      public long getValue() {
        return value_;
      }
      /**
       * <code>optional fixed64 value = 1;</code>
       */
      public Builder setValue(long value) {
        bitField0_ |= 0x00000001;
        value_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional fixed64 value = 1;</code>
       */
      public Builder clearValue() {
        bitField0_ = (bitField0_ & ~0x00000001);
        value_ = 0L;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:Fixed64)
    }

    static {
      defaultInstance = new Fixed64(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:Fixed64)
  }

  public interface StringOrBuilder extends
      // @@protoc_insertion_point(interface_extends:String)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string value = 1;</code>
     */
    boolean hasValue();
    /**
     * <code>optional string value = 1;</code>
     */
    java.lang.String getValue();
    /**
     * <code>optional string value = 1;</code>
     */
    com.google.protobuf.ByteString
        getValueBytes();
  }
  /**
   * Protobuf type {@code String}
   */
  public static final class String extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:String)
      StringOrBuilder {
    // Use String.newBuilder() to construct.
    private String(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private String(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final String defaultInstance;
    public static String getDefaultInstance() {
      return defaultInstance;
    }

    public String getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private String(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
              value_ = bs;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_String_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_String_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.String.class, CK.String.Builder.class);
    }

    public static com.google.protobuf.Parser<String> PARSER =
        new com.google.protobuf.AbstractParser<String>() {
      public String parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new String(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<String> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int VALUE_FIELD_NUMBER = 1;
    private java.lang.Object value_;
    /**
     * <code>optional string value = 1;</code>
     */
    public boolean hasValue() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional string value = 1;</code>
     */
    public java.lang.String getValue() {
      java.lang.Object ref = value_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          value_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string value = 1;</code>
     */
    public com.google.protobuf.ByteString
        getValueBytes() {
      java.lang.Object ref = value_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        value_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private void initFields() {
      value_ = "";
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getValueBytes());
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getValueBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.String parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.String parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.String parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.String parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.String parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.String parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.String parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.String parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.String parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.String parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.String prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code String}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:String)
        CK.StringOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_String_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_String_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.String.class, CK.String.Builder.class);
      }

      // Construct using CloudKit.String.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        value_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_String_descriptor;
      }

      public CK.String getDefaultInstanceForType() {
        return CK.String.getDefaultInstance();
      }

      public CK.String build() {
        CK.String result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.String buildPartial() {
        CK.String result = new CK.String(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.value_ = value_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.String) {
          return mergeFrom((CK.String)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.String other) {
        if (other == CK.String.getDefaultInstance()) return this;
        if (other.hasValue()) {
          bitField0_ |= 0x00000001;
          value_ = other.value_;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.String parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.String) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object value_ = "";
      /**
       * <code>optional string value = 1;</code>
       */
      public boolean hasValue() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional string value = 1;</code>
       */
      public java.lang.String getValue() {
        java.lang.Object ref = value_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            value_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string value = 1;</code>
       */
      public com.google.protobuf.ByteString
          getValueBytes() {
        java.lang.Object ref = value_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          value_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string value = 1;</code>
       */
      public Builder setValue(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        value_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string value = 1;</code>
       */
      public Builder clearValue() {
        bitField0_ = (bitField0_ & ~0x00000001);
        value_ = getDefaultInstance().getValue();
        onChanged();
        return this;
      }
      /**
       * <code>optional string value = 1;</code>
       */
      public Builder setValueBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        value_ = value;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:String)
    }

    static {
      defaultInstance = new String(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:String)
  }

  public interface UInt32OrBuilder extends
      // @@protoc_insertion_point(interface_extends:UInt32)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional uint32 value = 1;</code>
     */
    boolean hasValue();
    /**
     * <code>optional uint32 value = 1;</code>
     */
    int getValue();
  }
  /**
   * Protobuf type {@code UInt32}
   */
  public static final class UInt32 extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:UInt32)
      UInt32OrBuilder {
    // Use UInt32.newBuilder() to construct.
    private UInt32(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private UInt32(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final UInt32 defaultInstance;
    public static UInt32 getDefaultInstance() {
      return defaultInstance;
    }

    public UInt32 getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private UInt32(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
            case 8: {
              bitField0_ |= 0x00000001;
              value_ = input.readUInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return CK.internal_static_UInt32_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CK.internal_static_UInt32_fieldAccessorTable
          .ensureFieldAccessorsInitialized(CK.UInt32.class, CK.UInt32.Builder.class);
    }

    public static com.google.protobuf.Parser<UInt32> PARSER =
        new com.google.protobuf.AbstractParser<UInt32>() {
      public UInt32 parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new UInt32(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<UInt32> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int VALUE_FIELD_NUMBER = 1;
    private int value_;
    /**
     * <code>optional uint32 value = 1;</code>
     */
    public boolean hasValue() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional uint32 value = 1;</code>
     */
    public int getValue() {
      return value_;
    }

    private void initFields() {
      value_ = 0;
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeUInt32(1, value_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(1, value_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static CK.UInt32 parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.UInt32 parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.UInt32 parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CK.UInt32 parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CK.UInt32 parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.UInt32 parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static CK.UInt32 parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static CK.UInt32 parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static CK.UInt32 parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static CK.UInt32 parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(CK.UInt32 prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code UInt32}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:UInt32)
        CK.UInt32OrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CK.internal_static_UInt32_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CK.internal_static_UInt32_fieldAccessorTable
            .ensureFieldAccessorsInitialized(CK.UInt32.class, CK.UInt32.Builder.class);
      }

      // Construct using CloudKit.UInt32.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        value_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CK.internal_static_UInt32_descriptor;
      }

      public CK.UInt32 getDefaultInstanceForType() {
        return CK.UInt32.getDefaultInstance();
      }

      public CK.UInt32 build() {
        CK.UInt32 result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CK.UInt32 buildPartial() {
        CK.UInt32 result = new CK.UInt32(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.value_ = value_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof CK.UInt32) {
          return mergeFrom((CK.UInt32)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CK.UInt32 other) {
        if (other == CK.UInt32.getDefaultInstance()) return this;
        if (other.hasValue()) {
          setValue(other.getValue());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        CK.UInt32 parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CK.UInt32) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int value_ ;
      /**
       * <code>optional uint32 value = 1;</code>
       */
      public boolean hasValue() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional uint32 value = 1;</code>
       */
      public int getValue() {
        return value_;
      }
      /**
       * <code>optional uint32 value = 1;</code>
       */
      public Builder setValue(int value) {
        bitField0_ |= 0x00000001;
        value_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 value = 1;</code>
       */
      public Builder clearValue() {
        bitField0_ = (bitField0_ & ~0x00000001);
        value_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:UInt32)
    }

    static {
      defaultInstance = new UInt32(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:UInt32)
  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Request_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Request_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Response_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Response_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Info_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Info_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Message_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Message_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_M201Request_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_M201Request_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_M201Response_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_M201Response_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_M201ResponseBody_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_M201ResponseBody_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_M211Request_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_M211Request_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_M211Response_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_M211Response_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_M211ResponseBody_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_M211ResponseBody_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Status_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Status_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Error_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Error_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Container_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Container_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Data_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Data_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_IdItemOp_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_IdItemOp_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ItemOp_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_ItemOp_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_OpResult_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_OpResult_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Item_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Item_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Op_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Op_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_BytesString_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_BytesString_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Fixed64Pair_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Fixed64Pair_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Fixed64_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Fixed64_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_String_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_String_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_UInt32_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_UInt32_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\017cloud_kit.proto\"\201\001\n\007Request\022\023\n\004info\030\001 " +
      "\001(\0132\005.Info\022\031\n\007message\030\002 \001(\0132\010.Message\022\"\n" +
      "\013m201Request\030\311\001 \001(\0132\014.M201Request\022\"\n\013m21" +
      "1Request\030\323\001 \001(\0132\014.M211Request\"\226\001\n\010Respon" +
      "se\022\n\n\002f1\030\001 \001(\r\022\031\n\007message\030\002 \001(\0132\010.Messag" +
      "e\022\027\n\006status\030\003 \001(\0132\007.Status\022$\n\014m201Respon" +
      "se\030\311\001 \001(\0132\r.M201Response\022$\n\014m211Response" +
      "\030\323\001 \001(\0132\r.M211Response\"\237\002\n\004Info\022\021\n\tconta" +
      "iner\030\002 \001(\t\022\016\n\006bundle\030\003 \001(\t\022\021\n\002f7\030\007 \001(\0132\005" +
      ".Item\022\n\n\002os\030\010 \001(\t\022\024\n\002f9\030\t \001(\0132\010.Fixed64\022",
      "\013\n\003app\030\n \001(\t\022\022\n\nappVersion\030\013 \001(\t\022\021\n\toper" +
      "ation\030\014 \001(\t\022\016\n\006limit1\030\r \001(\r\022\016\n\006limit2\030\016 " +
      "\001(\r\022\r\n\005hex32\030\017 \001(\007\022\017\n\007version\030\022 \001(\t\022\013\n\003f" +
      "19\030\023 \001(\r\022\022\n\ndeviceName\030\025 \001(\t\022\020\n\010deviceID" +
      "\030\026 \001(\t\022\013\n\003f23\030\027 \001(\r\022\013\n\003f25\030\031 \001(\r\"1\n\007Mess" +
      "age\022\014\n\004uuid\030\001 \001(\t\022\014\n\004type\030\002 \001(\r\022\n\n\002f4\030\004 " +
      "\001(\r\"\036\n\013M201Request\022\017\n\002op\030\001 \001(\0132\003.Op\"/\n\014M" +
      "201Response\022\037\n\004body\030\001 \001(\0132\021.M201Response" +
      "Body\"]\n\020M201ResponseBody\022\031\n\006result\030\001 \001(\013" +
      "2\t.OpResult\022\n\n\002f2\030\002 \001(\014\022\n\n\002f4\030\004 \001(\r\022\n\n\002f",
      "5\030\005 \001(\r\022\n\n\002f6\030\006 \001(\r\";\n\013M211Request\022\027\n\006it" +
      "emOp\030\001 \001(\0132\007.ItemOp\022\023\n\002f6\030\006 \001(\0132\007.UInt32" +
      "\";\n\014M211Response\022\037\n\004body\030\001 \001(\0132\021.M211Res" +
      "ponseBody\022\n\n\002f2\030\002 \001(\r\"\365\001\n\020M211ResponseBo" +
      "dy\022\n\n\002f1\030\001 \001(\t\022\027\n\006itemOp\030\002 \001(\0132\007.ItemOp\022" +
      "\027\n\006record\030\003 \001(\0132\007.String\022\025\n\006userID\030\004 \001(\013" +
      "2\005.Item\022\030\n\002f5\030\005 \001(\0132\014.Fixed64Pair\022\035\n\tcon" +
      "tainer\030\007 \003(\0132\n.Container\022\027\n\010ckUserID\030\t \001" +
      "(\0132\005.Item\022\022\n\ndeviceName\030\013 \001(\t\022\031\n\003f13\030\r \001" +
      "(\0132\014.BytesString\022\013\n\003f15\030\017 \001(\r\"-\n\006Status\022",
      "\014\n\004code\030\001 \001(\r\022\025\n\005error\030\002 \001(\0132\006.Error\";\n\005" +
      "Error\022\025\n\004code\030\001 \001(\0132\007.UInt32\022\017\n\007message\030" +
      "\004 \001(\t\022\n\n\002id\030\005 \001(\t\"6\n\tContainer\022\024\n\003tag\030\001 " +
      "\001(\0132\007.String\022\023\n\004data\030\002 \001(\0132\005.Data\"\216\001\n\004Da" +
      "ta\022\n\n\002id\030\001 \001(\r\022\r\n\005bytes\030\002 \001(\014\022\016\n\006uint32\030" +
      "\004 \001(\r\022\031\n\007fixed64\030\006 \001(\0132\010.Fixed64\022\016\n\006stri" +
      "ng\030\007 \001(\t\022\033\n\010idItemOp\030\t \001(\0132\t.IdItemOp\022\023\n" +
      "\004data\030\013 \003(\0132\005.Data\"/\n\010IdItemOp\022\n\n\002id\030\001 \001" +
      "(\r\022\027\n\006itemOp\030\002 \001(\0132\007.ItemOp\".\n\006ItemOp\022\023\n" +
      "\004item\030\001 \001(\0132\005.Item\022\017\n\002op\030\002 \001(\0132\003.Op\">\n\010O",
      "pResult\022\017\n\002op\030\001 \001(\0132\003.Op\022!\n\013bytesString\030" +
      "\003 \001(\0132\014.BytesString\"#\n\004Item\022\r\n\005value\030\001 \001" +
      "(\t\022\014\n\004type\030\002 \001(\r\"2\n\002Op\022\023\n\004item\030\001 \001(\0132\005.I" +
      "tem\022\027\n\010ckUserID\030\002 \001(\0132\005.Item\",\n\013BytesStr" +
      "ing\022\r\n\005bytes\030\001 \001(\014\022\016\n\006string\030\002 \001(\t\";\n\013Fi" +
      "xed64Pair\022\025\n\003one\030\001 \001(\0132\010.Fixed64\022\025\n\003two\030" +
      "\002 \001(\0132\010.Fixed64\"\030\n\007Fixed64\022\r\n\005value\030\001 \001(" +
      "\006\"\027\n\006String\022\r\n\005value\030\001 \001(\t\"\027\n\006UInt32\022\r\n\005" +
      "value\030\001 \001(\r"
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
    internal_static_Request_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_Request_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_Request_descriptor,
        new java.lang.String[] { "Info", "Message", "M201Request", "M211Request", });
    internal_static_Response_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_Response_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_Response_descriptor,
        new java.lang.String[] { "F1", "Message", "Status", "M201Response", "M211Response", });
    internal_static_Info_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_Info_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_Info_descriptor,
        new java.lang.String[] { "Container", "Bundle", "F7", "Os", "F9", "App", "AppVersion", "Operation", "Limit1", "Limit2", "Hex32", "Version", "F19", "DeviceName", "DeviceID", "F23", "F25", });
    internal_static_Message_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_Message_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_Message_descriptor,
        new java.lang.String[] { "Uuid", "Type", "F4", });
    internal_static_M201Request_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_M201Request_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_M201Request_descriptor,
        new java.lang.String[] { "Op", });
    internal_static_M201Response_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_M201Response_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_M201Response_descriptor,
        new java.lang.String[] { "Body", });
    internal_static_M201ResponseBody_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_M201ResponseBody_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_M201ResponseBody_descriptor,
        new java.lang.String[] { "Result", "F2", "F4", "F5", "F6", });
    internal_static_M211Request_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_M211Request_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_M211Request_descriptor,
        new java.lang.String[] { "ItemOp", "F6", });
    internal_static_M211Response_descriptor =
      getDescriptor().getMessageTypes().get(8);
    internal_static_M211Response_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_M211Response_descriptor,
        new java.lang.String[] { "Body", "F2", });
    internal_static_M211ResponseBody_descriptor =
      getDescriptor().getMessageTypes().get(9);
    internal_static_M211ResponseBody_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_M211ResponseBody_descriptor,
        new java.lang.String[] { "F1", "ItemOp", "Record", "UserID", "F5", "Container", "CkUserID", "DeviceName", "F13", "F15", });
    internal_static_Status_descriptor =
      getDescriptor().getMessageTypes().get(10);
    internal_static_Status_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_Status_descriptor,
        new java.lang.String[] { "Code", "Error", });
    internal_static_Error_descriptor =
      getDescriptor().getMessageTypes().get(11);
    internal_static_Error_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_Error_descriptor,
        new java.lang.String[] { "Code", "Message", "Id", });
    internal_static_Container_descriptor =
      getDescriptor().getMessageTypes().get(12);
    internal_static_Container_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_Container_descriptor,
        new java.lang.String[] { "Tag", "Data", });
    internal_static_Data_descriptor =
      getDescriptor().getMessageTypes().get(13);
    internal_static_Data_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_Data_descriptor,
        new java.lang.String[] { "Id", "Bytes", "Uint32", "Fixed64", "String", "IdItemOp", "Data", });
    internal_static_IdItemOp_descriptor =
      getDescriptor().getMessageTypes().get(14);
    internal_static_IdItemOp_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_IdItemOp_descriptor,
        new java.lang.String[] { "Id", "ItemOp", });
    internal_static_ItemOp_descriptor =
      getDescriptor().getMessageTypes().get(15);
    internal_static_ItemOp_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_ItemOp_descriptor,
        new java.lang.String[] { "Item", "Op", });
    internal_static_OpResult_descriptor =
      getDescriptor().getMessageTypes().get(16);
    internal_static_OpResult_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_OpResult_descriptor,
        new java.lang.String[] { "Op", "BytesString", });
    internal_static_Item_descriptor =
      getDescriptor().getMessageTypes().get(17);
    internal_static_Item_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_Item_descriptor,
        new java.lang.String[] { "Value", "Type", });
    internal_static_Op_descriptor =
      getDescriptor().getMessageTypes().get(18);
    internal_static_Op_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_Op_descriptor,
        new java.lang.String[] { "Item", "CkUserID", });
    internal_static_BytesString_descriptor =
      getDescriptor().getMessageTypes().get(19);
    internal_static_BytesString_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_BytesString_descriptor,
        new java.lang.String[] { "Bytes", "String", });
    internal_static_Fixed64Pair_descriptor =
      getDescriptor().getMessageTypes().get(20);
    internal_static_Fixed64Pair_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_Fixed64Pair_descriptor,
        new java.lang.String[] { "One", "Two", });
    internal_static_Fixed64_descriptor =
      getDescriptor().getMessageTypes().get(21);
    internal_static_Fixed64_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_Fixed64_descriptor,
        new java.lang.String[] { "Value", });
    internal_static_String_descriptor =
      getDescriptor().getMessageTypes().get(22);
    internal_static_String_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_String_descriptor,
        new java.lang.String[] { "Value", });
    internal_static_UInt32_descriptor =
      getDescriptor().getMessageTypes().get(23);
    internal_static_UInt32_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_UInt32_descriptor,
        new java.lang.String[] { "Value", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
