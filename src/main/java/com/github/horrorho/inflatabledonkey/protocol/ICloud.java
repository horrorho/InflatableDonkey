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

public final class ICloud {
  private ICloud() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface MBSAccountOrBuilder extends
      // @@protoc_insertion_point(interface_extends:MBSAccount)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string AccountID = 1;</code>
     */
    boolean hasAccountID();
    /**
     * <code>optional string AccountID = 1;</code>
     */
    java.lang.String getAccountID();
    /**
     * <code>optional string AccountID = 1;</code>
     */
    com.google.protobuf.ByteString
        getAccountIDBytes();

    /**
     * <code>repeated bytes backupUDID = 2;</code>
     */
    java.util.List<com.google.protobuf.ByteString> getBackupUDIDList();
    /**
     * <code>repeated bytes backupUDID = 2;</code>
     */
    int getBackupUDIDCount();
    /**
     * <code>repeated bytes backupUDID = 2;</code>
     */
    com.google.protobuf.ByteString getBackupUDID(int index);
  }
  /**
   * Protobuf type {@code MBSAccount}
   */
  public static final class MBSAccount extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:MBSAccount)
      MBSAccountOrBuilder {
    // Use MBSAccount.newBuilder() to construct.
    private MBSAccount(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private MBSAccount(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final MBSAccount defaultInstance;
    public static MBSAccount getDefaultInstance() {
      return defaultInstance;
    }

    public MBSAccount getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private MBSAccount(
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
              accountID_ = bs;
              break;
            }
            case 18: {
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
                backupUDID_ = new java.util.ArrayList<com.google.protobuf.ByteString>();
                mutable_bitField0_ |= 0x00000002;
              }
              backupUDID_.add(input.readBytes());
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
        if (((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
          backupUDID_ = java.util.Collections.unmodifiableList(backupUDID_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return ICloud.internal_static_MBSAccount_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ICloud.internal_static_MBSAccount_fieldAccessorTable
          .ensureFieldAccessorsInitialized(ICloud.MBSAccount.class, ICloud.MBSAccount.Builder.class);
    }

    public static com.google.protobuf.Parser<MBSAccount> PARSER =
        new com.google.protobuf.AbstractParser<MBSAccount>() {
      public MBSAccount parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MBSAccount(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<MBSAccount> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int ACCOUNTID_FIELD_NUMBER = 1;
    private java.lang.Object accountID_;
    /**
     * <code>optional string AccountID = 1;</code>
     */
    public boolean hasAccountID() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional string AccountID = 1;</code>
     */
    public java.lang.String getAccountID() {
      java.lang.Object ref = accountID_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          accountID_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string AccountID = 1;</code>
     */
    public com.google.protobuf.ByteString
        getAccountIDBytes() {
      java.lang.Object ref = accountID_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        accountID_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int BACKUPUDID_FIELD_NUMBER = 2;
    private java.util.List<com.google.protobuf.ByteString> backupUDID_;
    /**
     * <code>repeated bytes backupUDID = 2;</code>
     */
    public java.util.List<com.google.protobuf.ByteString>
        getBackupUDIDList() {
      return backupUDID_;
    }
    /**
     * <code>repeated bytes backupUDID = 2;</code>
     */
    public int getBackupUDIDCount() {
      return backupUDID_.size();
    }
    /**
     * <code>repeated bytes backupUDID = 2;</code>
     */
    public com.google.protobuf.ByteString getBackupUDID(int index) {
      return backupUDID_.get(index);
    }

    private void initFields() {
      accountID_ = "";
      backupUDID_ = java.util.Collections.emptyList();
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
        output.writeBytes(1, getAccountIDBytes());
      }
      for (int i = 0; i < backupUDID_.size(); i++) {
        output.writeBytes(2, backupUDID_.get(i));
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
          .computeBytesSize(1, getAccountIDBytes());
      }
      {
        int dataSize = 0;
        for (int i = 0; i < backupUDID_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeBytesSizeNoTag(backupUDID_.get(i));
        }
        size += dataSize;
        size += 1 * getBackupUDIDList().size();
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

    public static ICloud.MBSAccount parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSAccount parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSAccount parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSAccount parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSAccount parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSAccount parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static ICloud.MBSAccount parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static ICloud.MBSAccount parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static ICloud.MBSAccount parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSAccount parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(ICloud.MBSAccount prototype) {
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
     * Protobuf type {@code MBSAccount}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:MBSAccount)
        ICloud.MBSAccountOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ICloud.internal_static_MBSAccount_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ICloud.internal_static_MBSAccount_fieldAccessorTable
            .ensureFieldAccessorsInitialized(ICloud.MBSAccount.class, ICloud.MBSAccount.Builder.class);
      }

      // Construct using Icloud.MBSAccount.newBuilder()
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
        accountID_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        backupUDID_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ICloud.internal_static_MBSAccount_descriptor;
      }

      public ICloud.MBSAccount getDefaultInstanceForType() {
        return ICloud.MBSAccount.getDefaultInstance();
      }

      public ICloud.MBSAccount build() {
        ICloud.MBSAccount result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ICloud.MBSAccount buildPartial() {
        ICloud.MBSAccount result = new ICloud.MBSAccount(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.accountID_ = accountID_;
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          backupUDID_ = java.util.Collections.unmodifiableList(backupUDID_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.backupUDID_ = backupUDID_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ICloud.MBSAccount) {
          return mergeFrom((ICloud.MBSAccount)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ICloud.MBSAccount other) {
        if (other == ICloud.MBSAccount.getDefaultInstance()) return this;
        if (other.hasAccountID()) {
          bitField0_ |= 0x00000001;
          accountID_ = other.accountID_;
          onChanged();
        }
        if (!other.backupUDID_.isEmpty()) {
          if (backupUDID_.isEmpty()) {
            backupUDID_ = other.backupUDID_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensureBackupUDIDIsMutable();
            backupUDID_.addAll(other.backupUDID_);
          }
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
        ICloud.MBSAccount parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ICloud.MBSAccount) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object accountID_ = "";
      /**
       * <code>optional string AccountID = 1;</code>
       */
      public boolean hasAccountID() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional string AccountID = 1;</code>
       */
      public java.lang.String getAccountID() {
        java.lang.Object ref = accountID_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            accountID_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string AccountID = 1;</code>
       */
      public com.google.protobuf.ByteString
          getAccountIDBytes() {
        java.lang.Object ref = accountID_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          accountID_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string AccountID = 1;</code>
       */
      public Builder setAccountID(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        accountID_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string AccountID = 1;</code>
       */
      public Builder clearAccountID() {
        bitField0_ = (bitField0_ & ~0x00000001);
        accountID_ = getDefaultInstance().getAccountID();
        onChanged();
        return this;
      }
      /**
       * <code>optional string AccountID = 1;</code>
       */
      public Builder setAccountIDBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        accountID_ = value;
        onChanged();
        return this;
      }

      private java.util.List<com.google.protobuf.ByteString> backupUDID_ = java.util.Collections.emptyList();
      private void ensureBackupUDIDIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          backupUDID_ = new java.util.ArrayList<com.google.protobuf.ByteString>(backupUDID_);
          bitField0_ |= 0x00000002;
         }
      }
      /**
       * <code>repeated bytes backupUDID = 2;</code>
       */
      public java.util.List<com.google.protobuf.ByteString>
          getBackupUDIDList() {
        return java.util.Collections.unmodifiableList(backupUDID_);
      }
      /**
       * <code>repeated bytes backupUDID = 2;</code>
       */
      public int getBackupUDIDCount() {
        return backupUDID_.size();
      }
      /**
       * <code>repeated bytes backupUDID = 2;</code>
       */
      public com.google.protobuf.ByteString getBackupUDID(int index) {
        return backupUDID_.get(index);
      }
      /**
       * <code>repeated bytes backupUDID = 2;</code>
       */
      public Builder setBackupUDID(
          int index, com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureBackupUDIDIsMutable();
        backupUDID_.set(index, value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated bytes backupUDID = 2;</code>
       */
      public Builder addBackupUDID(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureBackupUDIDIsMutable();
        backupUDID_.add(value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated bytes backupUDID = 2;</code>
       */
      public Builder addAllBackupUDID(
          java.lang.Iterable<? extends com.google.protobuf.ByteString> values) {
        ensureBackupUDIDIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, backupUDID_);
        onChanged();
        return this;
      }
      /**
       * <code>repeated bytes backupUDID = 2;</code>
       */
      public Builder clearBackupUDID() {
        backupUDID_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:MBSAccount)
    }

    static {
      defaultInstance = new MBSAccount(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:MBSAccount)
  }

  public interface MBSBackupOrBuilder extends
      // @@protoc_insertion_point(interface_extends:MBSBackup)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional bytes backupUDID = 1;</code>
     */
    boolean hasBackupUDID();
    /**
     * <code>optional bytes backupUDID = 1;</code>
     */
    com.google.protobuf.ByteString getBackupUDID();

    /**
     * <code>optional uint64 QuotaUsed = 2;</code>
     */
    boolean hasQuotaUsed();
    /**
     * <code>optional uint64 QuotaUsed = 2;</code>
     */
    long getQuotaUsed();

    /**
     * <code>repeated .MBSSnapshot Snapshot = 3;</code>
     */
    java.util.List<ICloud.MBSSnapshot> 
        getSnapshotList();
    /**
     * <code>repeated .MBSSnapshot Snapshot = 3;</code>
     */
    ICloud.MBSSnapshot getSnapshot(int index);
    /**
     * <code>repeated .MBSSnapshot Snapshot = 3;</code>
     */
    int getSnapshotCount();
    /**
     * <code>repeated .MBSSnapshot Snapshot = 3;</code>
     */
    java.util.List<? extends ICloud.MBSSnapshotOrBuilder> 
        getSnapshotOrBuilderList();
    /**
     * <code>repeated .MBSSnapshot Snapshot = 3;</code>
     */
    ICloud.MBSSnapshotOrBuilder getSnapshotOrBuilder(
        int index);

    /**
     * <code>optional .MBSBackupAttributes Attributes = 4;</code>
     */
    boolean hasAttributes();
    /**
     * <code>optional .MBSBackupAttributes Attributes = 4;</code>
     */
    ICloud.MBSBackupAttributes getAttributes();
    /**
     * <code>optional .MBSBackupAttributes Attributes = 4;</code>
     */
    ICloud.MBSBackupAttributesOrBuilder getAttributesOrBuilder();

    /**
     * <code>optional uint64 KeysLastModified = 5;</code>
     */
    boolean hasKeysLastModified();
    /**
     * <code>optional uint64 KeysLastModified = 5;</code>
     */
    long getKeysLastModified();
  }
  /**
   * Protobuf type {@code MBSBackup}
   */
  public static final class MBSBackup extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:MBSBackup)
      MBSBackupOrBuilder {
    // Use MBSBackup.newBuilder() to construct.
    private MBSBackup(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private MBSBackup(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final MBSBackup defaultInstance;
    public static MBSBackup getDefaultInstance() {
      return defaultInstance;
    }

    public MBSBackup getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private MBSBackup(
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
              backupUDID_ = input.readBytes();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              quotaUsed_ = input.readUInt64();
              break;
            }
            case 26: {
              if (!((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
                snapshot_ = new java.util.ArrayList<ICloud.MBSSnapshot>();
                mutable_bitField0_ |= 0x00000004;
              }
              snapshot_.add(input.readMessage(ICloud.MBSSnapshot.PARSER, extensionRegistry));
              break;
            }
            case 34: {
              ICloud.MBSBackupAttributes.Builder subBuilder = null;
              if (((bitField0_ & 0x00000004) == 0x00000004)) {
                subBuilder = attributes_.toBuilder();
              }
              attributes_ = input.readMessage(ICloud.MBSBackupAttributes.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(attributes_);
                attributes_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000004;
              break;
            }
            case 40: {
              bitField0_ |= 0x00000008;
              keysLastModified_ = input.readUInt64();
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
        if (((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
          snapshot_ = java.util.Collections.unmodifiableList(snapshot_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return ICloud.internal_static_MBSBackup_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ICloud.internal_static_MBSBackup_fieldAccessorTable
          .ensureFieldAccessorsInitialized(ICloud.MBSBackup.class, ICloud.MBSBackup.Builder.class);
    }

    public static com.google.protobuf.Parser<MBSBackup> PARSER =
        new com.google.protobuf.AbstractParser<MBSBackup>() {
      public MBSBackup parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MBSBackup(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<MBSBackup> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int BACKUPUDID_FIELD_NUMBER = 1;
    private com.google.protobuf.ByteString backupUDID_;
    /**
     * <code>optional bytes backupUDID = 1;</code>
     */
    public boolean hasBackupUDID() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional bytes backupUDID = 1;</code>
     */
    public com.google.protobuf.ByteString getBackupUDID() {
      return backupUDID_;
    }

    public static final int QUOTAUSED_FIELD_NUMBER = 2;
    private long quotaUsed_;
    /**
     * <code>optional uint64 QuotaUsed = 2;</code>
     */
    public boolean hasQuotaUsed() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional uint64 QuotaUsed = 2;</code>
     */
    public long getQuotaUsed() {
      return quotaUsed_;
    }

    public static final int SNAPSHOT_FIELD_NUMBER = 3;
    private java.util.List<ICloud.MBSSnapshot> snapshot_;
    /**
     * <code>repeated .MBSSnapshot Snapshot = 3;</code>
     */
    public java.util.List<ICloud.MBSSnapshot> getSnapshotList() {
      return snapshot_;
    }
    /**
     * <code>repeated .MBSSnapshot Snapshot = 3;</code>
     */
    public java.util.List<? extends ICloud.MBSSnapshotOrBuilder> 
        getSnapshotOrBuilderList() {
      return snapshot_;
    }
    /**
     * <code>repeated .MBSSnapshot Snapshot = 3;</code>
     */
    public int getSnapshotCount() {
      return snapshot_.size();
    }
    /**
     * <code>repeated .MBSSnapshot Snapshot = 3;</code>
     */
    public ICloud.MBSSnapshot getSnapshot(int index) {
      return snapshot_.get(index);
    }
    /**
     * <code>repeated .MBSSnapshot Snapshot = 3;</code>
     */
    public ICloud.MBSSnapshotOrBuilder getSnapshotOrBuilder(
        int index) {
      return snapshot_.get(index);
    }

    public static final int ATTRIBUTES_FIELD_NUMBER = 4;
    private ICloud.MBSBackupAttributes attributes_;
    /**
     * <code>optional .MBSBackupAttributes Attributes = 4;</code>
     */
    public boolean hasAttributes() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional .MBSBackupAttributes Attributes = 4;</code>
     */
    public ICloud.MBSBackupAttributes getAttributes() {
      return attributes_;
    }
    /**
     * <code>optional .MBSBackupAttributes Attributes = 4;</code>
     */
    public ICloud.MBSBackupAttributesOrBuilder getAttributesOrBuilder() {
      return attributes_;
    }

    public static final int KEYSLASTMODIFIED_FIELD_NUMBER = 5;
    private long keysLastModified_;
    /**
     * <code>optional uint64 KeysLastModified = 5;</code>
     */
    public boolean hasKeysLastModified() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional uint64 KeysLastModified = 5;</code>
     */
    public long getKeysLastModified() {
      return keysLastModified_;
    }

    private void initFields() {
      backupUDID_ = com.google.protobuf.ByteString.EMPTY;
      quotaUsed_ = 0L;
      snapshot_ = java.util.Collections.emptyList();
      attributes_ = ICloud.MBSBackupAttributes.getDefaultInstance();
      keysLastModified_ = 0L;
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
        output.writeBytes(1, backupUDID_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeUInt64(2, quotaUsed_);
      }
      for (int i = 0; i < snapshot_.size(); i++) {
        output.writeMessage(3, snapshot_.get(i));
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeMessage(4, attributes_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeUInt64(5, keysLastModified_);
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
          .computeBytesSize(1, backupUDID_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(2, quotaUsed_);
      }
      for (int i = 0; i < snapshot_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, snapshot_.get(i));
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, attributes_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(5, keysLastModified_);
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

    public static ICloud.MBSBackup parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSBackup parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSBackup parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSBackup parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSBackup parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSBackup parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static ICloud.MBSBackup parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static ICloud.MBSBackup parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static ICloud.MBSBackup parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSBackup parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(ICloud.MBSBackup prototype) {
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
     * Protobuf type {@code MBSBackup}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:MBSBackup)
        ICloud.MBSBackupOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ICloud.internal_static_MBSBackup_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ICloud.internal_static_MBSBackup_fieldAccessorTable
            .ensureFieldAccessorsInitialized(ICloud.MBSBackup.class, ICloud.MBSBackup.Builder.class);
      }

      // Construct using Icloud.MBSBackup.newBuilder()
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
          getSnapshotFieldBuilder();
          getAttributesFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        backupUDID_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        quotaUsed_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000002);
        if (snapshotBuilder_ == null) {
          snapshot_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000004);
        } else {
          snapshotBuilder_.clear();
        }
        if (attributesBuilder_ == null) {
          attributes_ = ICloud.MBSBackupAttributes.getDefaultInstance();
        } else {
          attributesBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        keysLastModified_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ICloud.internal_static_MBSBackup_descriptor;
      }

      public ICloud.MBSBackup getDefaultInstanceForType() {
        return ICloud.MBSBackup.getDefaultInstance();
      }

      public ICloud.MBSBackup build() {
        ICloud.MBSBackup result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ICloud.MBSBackup buildPartial() {
        ICloud.MBSBackup result = new ICloud.MBSBackup(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.backupUDID_ = backupUDID_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.quotaUsed_ = quotaUsed_;
        if (snapshotBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004)) {
            snapshot_ = java.util.Collections.unmodifiableList(snapshot_);
            bitField0_ = (bitField0_ & ~0x00000004);
          }
          result.snapshot_ = snapshot_;
        } else {
          result.snapshot_ = snapshotBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000004;
        }
        if (attributesBuilder_ == null) {
          result.attributes_ = attributes_;
        } else {
          result.attributes_ = attributesBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000008;
        }
        result.keysLastModified_ = keysLastModified_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ICloud.MBSBackup) {
          return mergeFrom((ICloud.MBSBackup)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ICloud.MBSBackup other) {
        if (other == ICloud.MBSBackup.getDefaultInstance()) return this;
        if (other.hasBackupUDID()) {
          setBackupUDID(other.getBackupUDID());
        }
        if (other.hasQuotaUsed()) {
          setQuotaUsed(other.getQuotaUsed());
        }
        if (snapshotBuilder_ == null) {
          if (!other.snapshot_.isEmpty()) {
            if (snapshot_.isEmpty()) {
              snapshot_ = other.snapshot_;
              bitField0_ = (bitField0_ & ~0x00000004);
            } else {
              ensureSnapshotIsMutable();
              snapshot_.addAll(other.snapshot_);
            }
            onChanged();
          }
        } else {
          if (!other.snapshot_.isEmpty()) {
            if (snapshotBuilder_.isEmpty()) {
              snapshotBuilder_.dispose();
              snapshotBuilder_ = null;
              snapshot_ = other.snapshot_;
              bitField0_ = (bitField0_ & ~0x00000004);
              snapshotBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getSnapshotFieldBuilder() : null;
            } else {
              snapshotBuilder_.addAllMessages(other.snapshot_);
            }
          }
        }
        if (other.hasAttributes()) {
          mergeAttributes(other.getAttributes());
        }
        if (other.hasKeysLastModified()) {
          setKeysLastModified(other.getKeysLastModified());
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
        ICloud.MBSBackup parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ICloud.MBSBackup) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.google.protobuf.ByteString backupUDID_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes backupUDID = 1;</code>
       */
      public boolean hasBackupUDID() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional bytes backupUDID = 1;</code>
       */
      public com.google.protobuf.ByteString getBackupUDID() {
        return backupUDID_;
      }
      /**
       * <code>optional bytes backupUDID = 1;</code>
       */
      public Builder setBackupUDID(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        backupUDID_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes backupUDID = 1;</code>
       */
      public Builder clearBackupUDID() {
        bitField0_ = (bitField0_ & ~0x00000001);
        backupUDID_ = getDefaultInstance().getBackupUDID();
        onChanged();
        return this;
      }

      private long quotaUsed_ ;
      /**
       * <code>optional uint64 QuotaUsed = 2;</code>
       */
      public boolean hasQuotaUsed() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional uint64 QuotaUsed = 2;</code>
       */
      public long getQuotaUsed() {
        return quotaUsed_;
      }
      /**
       * <code>optional uint64 QuotaUsed = 2;</code>
       */
      public Builder setQuotaUsed(long value) {
        bitField0_ |= 0x00000002;
        quotaUsed_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint64 QuotaUsed = 2;</code>
       */
      public Builder clearQuotaUsed() {
        bitField0_ = (bitField0_ & ~0x00000002);
        quotaUsed_ = 0L;
        onChanged();
        return this;
      }

      private java.util.List<ICloud.MBSSnapshot> snapshot_ =
        java.util.Collections.emptyList();
      private void ensureSnapshotIsMutable() {
        if (!((bitField0_ & 0x00000004) == 0x00000004)) {
          snapshot_ = new java.util.ArrayList<ICloud.MBSSnapshot>(snapshot_);
          bitField0_ |= 0x00000004;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          ICloud.MBSSnapshot, ICloud.MBSSnapshot.Builder, ICloud.MBSSnapshotOrBuilder> snapshotBuilder_;

      /**
       * <code>repeated .MBSSnapshot Snapshot = 3;</code>
       */
      public java.util.List<ICloud.MBSSnapshot> getSnapshotList() {
        if (snapshotBuilder_ == null) {
          return java.util.Collections.unmodifiableList(snapshot_);
        } else {
          return snapshotBuilder_.getMessageList();
        }
      }
      /**
       * <code>repeated .MBSSnapshot Snapshot = 3;</code>
       */
      public int getSnapshotCount() {
        if (snapshotBuilder_ == null) {
          return snapshot_.size();
        } else {
          return snapshotBuilder_.getCount();
        }
      }
      /**
       * <code>repeated .MBSSnapshot Snapshot = 3;</code>
       */
      public ICloud.MBSSnapshot getSnapshot(int index) {
        if (snapshotBuilder_ == null) {
          return snapshot_.get(index);
        } else {
          return snapshotBuilder_.getMessage(index);
        }
      }
      /**
       * <code>repeated .MBSSnapshot Snapshot = 3;</code>
       */
      public Builder setSnapshot(
          int index, ICloud.MBSSnapshot value) {
        if (snapshotBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureSnapshotIsMutable();
          snapshot_.set(index, value);
          onChanged();
        } else {
          snapshotBuilder_.setMessage(index, value);
        }
        return this;
      }
      /**
       * <code>repeated .MBSSnapshot Snapshot = 3;</code>
       */
      public Builder setSnapshot(
          int index, ICloud.MBSSnapshot.Builder builderForValue) {
        if (snapshotBuilder_ == null) {
          ensureSnapshotIsMutable();
          snapshot_.set(index, builderForValue.build());
          onChanged();
        } else {
          snapshotBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .MBSSnapshot Snapshot = 3;</code>
       */
      public Builder addSnapshot(ICloud.MBSSnapshot value) {
        if (snapshotBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureSnapshotIsMutable();
          snapshot_.add(value);
          onChanged();
        } else {
          snapshotBuilder_.addMessage(value);
        }
        return this;
      }
      /**
       * <code>repeated .MBSSnapshot Snapshot = 3;</code>
       */
      public Builder addSnapshot(
          int index, ICloud.MBSSnapshot value) {
        if (snapshotBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureSnapshotIsMutable();
          snapshot_.add(index, value);
          onChanged();
        } else {
          snapshotBuilder_.addMessage(index, value);
        }
        return this;
      }
      /**
       * <code>repeated .MBSSnapshot Snapshot = 3;</code>
       */
      public Builder addSnapshot(
          ICloud.MBSSnapshot.Builder builderForValue) {
        if (snapshotBuilder_ == null) {
          ensureSnapshotIsMutable();
          snapshot_.add(builderForValue.build());
          onChanged();
        } else {
          snapshotBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .MBSSnapshot Snapshot = 3;</code>
       */
      public Builder addSnapshot(
          int index, ICloud.MBSSnapshot.Builder builderForValue) {
        if (snapshotBuilder_ == null) {
          ensureSnapshotIsMutable();
          snapshot_.add(index, builderForValue.build());
          onChanged();
        } else {
          snapshotBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .MBSSnapshot Snapshot = 3;</code>
       */
      public Builder addAllSnapshot(
          java.lang.Iterable<? extends ICloud.MBSSnapshot> values) {
        if (snapshotBuilder_ == null) {
          ensureSnapshotIsMutable();
          com.google.protobuf.AbstractMessageLite.Builder.addAll(
              values, snapshot_);
          onChanged();
        } else {
          snapshotBuilder_.addAllMessages(values);
        }
        return this;
      }
      /**
       * <code>repeated .MBSSnapshot Snapshot = 3;</code>
       */
      public Builder clearSnapshot() {
        if (snapshotBuilder_ == null) {
          snapshot_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000004);
          onChanged();
        } else {
          snapshotBuilder_.clear();
        }
        return this;
      }
      /**
       * <code>repeated .MBSSnapshot Snapshot = 3;</code>
       */
      public Builder removeSnapshot(int index) {
        if (snapshotBuilder_ == null) {
          ensureSnapshotIsMutable();
          snapshot_.remove(index);
          onChanged();
        } else {
          snapshotBuilder_.remove(index);
        }
        return this;
      }
      /**
       * <code>repeated .MBSSnapshot Snapshot = 3;</code>
       */
      public ICloud.MBSSnapshot.Builder getSnapshotBuilder(
          int index) {
        return getSnapshotFieldBuilder().getBuilder(index);
      }
      /**
       * <code>repeated .MBSSnapshot Snapshot = 3;</code>
       */
      public ICloud.MBSSnapshotOrBuilder getSnapshotOrBuilder(
          int index) {
        if (snapshotBuilder_ == null) {
          return snapshot_.get(index);  } else {
          return snapshotBuilder_.getMessageOrBuilder(index);
        }
      }
      /**
       * <code>repeated .MBSSnapshot Snapshot = 3;</code>
       */
      public java.util.List<? extends ICloud.MBSSnapshotOrBuilder> 
           getSnapshotOrBuilderList() {
        if (snapshotBuilder_ != null) {
          return snapshotBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(snapshot_);
        }
      }
      /**
       * <code>repeated .MBSSnapshot Snapshot = 3;</code>
       */
      public ICloud.MBSSnapshot.Builder addSnapshotBuilder() {
        return getSnapshotFieldBuilder().addBuilder(ICloud.MBSSnapshot.getDefaultInstance());
      }
      /**
       * <code>repeated .MBSSnapshot Snapshot = 3;</code>
       */
      public ICloud.MBSSnapshot.Builder addSnapshotBuilder(
          int index) {
        return getSnapshotFieldBuilder().addBuilder(index, ICloud.MBSSnapshot.getDefaultInstance());
      }
      /**
       * <code>repeated .MBSSnapshot Snapshot = 3;</code>
       */
      public java.util.List<ICloud.MBSSnapshot.Builder> 
           getSnapshotBuilderList() {
        return getSnapshotFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          ICloud.MBSSnapshot, ICloud.MBSSnapshot.Builder, ICloud.MBSSnapshotOrBuilder> 
          getSnapshotFieldBuilder() {
        if (snapshotBuilder_ == null) {
          snapshotBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              ICloud.MBSSnapshot, ICloud.MBSSnapshot.Builder, ICloud.MBSSnapshotOrBuilder>(
                  snapshot_,
                  ((bitField0_ & 0x00000004) == 0x00000004),
                  getParentForChildren(),
                  isClean());
          snapshot_ = null;
        }
        return snapshotBuilder_;
      }

      private ICloud.MBSBackupAttributes attributes_ = ICloud.MBSBackupAttributes.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          ICloud.MBSBackupAttributes, ICloud.MBSBackupAttributes.Builder, ICloud.MBSBackupAttributesOrBuilder> attributesBuilder_;
      /**
       * <code>optional .MBSBackupAttributes Attributes = 4;</code>
       */
      public boolean hasAttributes() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>optional .MBSBackupAttributes Attributes = 4;</code>
       */
      public ICloud.MBSBackupAttributes getAttributes() {
        if (attributesBuilder_ == null) {
          return attributes_;
        } else {
          return attributesBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .MBSBackupAttributes Attributes = 4;</code>
       */
      public Builder setAttributes(ICloud.MBSBackupAttributes value) {
        if (attributesBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          attributes_ = value;
          onChanged();
        } else {
          attributesBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .MBSBackupAttributes Attributes = 4;</code>
       */
      public Builder setAttributes(
          ICloud.MBSBackupAttributes.Builder builderForValue) {
        if (attributesBuilder_ == null) {
          attributes_ = builderForValue.build();
          onChanged();
        } else {
          attributesBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .MBSBackupAttributes Attributes = 4;</code>
       */
      public Builder mergeAttributes(ICloud.MBSBackupAttributes value) {
        if (attributesBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008) &&
              attributes_ != ICloud.MBSBackupAttributes.getDefaultInstance()) {
            attributes_ =
              ICloud.MBSBackupAttributes.newBuilder(attributes_).mergeFrom(value).buildPartial();
          } else {
            attributes_ = value;
          }
          onChanged();
        } else {
          attributesBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .MBSBackupAttributes Attributes = 4;</code>
       */
      public Builder clearAttributes() {
        if (attributesBuilder_ == null) {
          attributes_ = ICloud.MBSBackupAttributes.getDefaultInstance();
          onChanged();
        } else {
          attributesBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      /**
       * <code>optional .MBSBackupAttributes Attributes = 4;</code>
       */
      public ICloud.MBSBackupAttributes.Builder getAttributesBuilder() {
        bitField0_ |= 0x00000008;
        onChanged();
        return getAttributesFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .MBSBackupAttributes Attributes = 4;</code>
       */
      public ICloud.MBSBackupAttributesOrBuilder getAttributesOrBuilder() {
        if (attributesBuilder_ != null) {
          return attributesBuilder_.getMessageOrBuilder();
        } else {
          return attributes_;
        }
      }
      /**
       * <code>optional .MBSBackupAttributes Attributes = 4;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          ICloud.MBSBackupAttributes, ICloud.MBSBackupAttributes.Builder, ICloud.MBSBackupAttributesOrBuilder> 
          getAttributesFieldBuilder() {
        if (attributesBuilder_ == null) {
          attributesBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              ICloud.MBSBackupAttributes, ICloud.MBSBackupAttributes.Builder, ICloud.MBSBackupAttributesOrBuilder>(
                  getAttributes(),
                  getParentForChildren(),
                  isClean());
          attributes_ = null;
        }
        return attributesBuilder_;
      }

      private long keysLastModified_ ;
      /**
       * <code>optional uint64 KeysLastModified = 5;</code>
       */
      public boolean hasKeysLastModified() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      /**
       * <code>optional uint64 KeysLastModified = 5;</code>
       */
      public long getKeysLastModified() {
        return keysLastModified_;
      }
      /**
       * <code>optional uint64 KeysLastModified = 5;</code>
       */
      public Builder setKeysLastModified(long value) {
        bitField0_ |= 0x00000010;
        keysLastModified_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint64 KeysLastModified = 5;</code>
       */
      public Builder clearKeysLastModified() {
        bitField0_ = (bitField0_ & ~0x00000010);
        keysLastModified_ = 0L;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:MBSBackup)
    }

    static {
      defaultInstance = new MBSBackup(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:MBSBackup)
  }

  public interface MBSBackupAttributesOrBuilder extends
      // @@protoc_insertion_point(interface_extends:MBSBackupAttributes)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string DeviceClass = 1;</code>
     */
    boolean hasDeviceClass();
    /**
     * <code>optional string DeviceClass = 1;</code>
     */
    java.lang.String getDeviceClass();
    /**
     * <code>optional string DeviceClass = 1;</code>
     */
    com.google.protobuf.ByteString
        getDeviceClassBytes();

    /**
     * <code>optional string ProductType = 2;</code>
     */
    boolean hasProductType();
    /**
     * <code>optional string ProductType = 2;</code>
     */
    java.lang.String getProductType();
    /**
     * <code>optional string ProductType = 2;</code>
     */
    com.google.protobuf.ByteString
        getProductTypeBytes();

    /**
     * <code>optional string SerialNumber = 3;</code>
     */
    boolean hasSerialNumber();
    /**
     * <code>optional string SerialNumber = 3;</code>
     */
    java.lang.String getSerialNumber();
    /**
     * <code>optional string SerialNumber = 3;</code>
     */
    com.google.protobuf.ByteString
        getSerialNumberBytes();

    /**
     * <code>optional string DeviceColor = 4;</code>
     */
    boolean hasDeviceColor();
    /**
     * <code>optional string DeviceColor = 4;</code>
     */
    java.lang.String getDeviceColor();
    /**
     * <code>optional string DeviceColor = 4;</code>
     */
    com.google.protobuf.ByteString
        getDeviceColorBytes();

    /**
     * <code>optional string HardwareModel = 5;</code>
     */
    boolean hasHardwareModel();
    /**
     * <code>optional string HardwareModel = 5;</code>
     */
    java.lang.String getHardwareModel();
    /**
     * <code>optional string HardwareModel = 5;</code>
     */
    com.google.protobuf.ByteString
        getHardwareModelBytes();

    /**
     * <code>optional string MarketingName = 6;</code>
     */
    boolean hasMarketingName();
    /**
     * <code>optional string MarketingName = 6;</code>
     */
    java.lang.String getMarketingName();
    /**
     * <code>optional string MarketingName = 6;</code>
     */
    com.google.protobuf.ByteString
        getMarketingNameBytes();
  }
  /**
   * Protobuf type {@code MBSBackupAttributes}
   */
  public static final class MBSBackupAttributes extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:MBSBackupAttributes)
      MBSBackupAttributesOrBuilder {
    // Use MBSBackupAttributes.newBuilder() to construct.
    private MBSBackupAttributes(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private MBSBackupAttributes(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final MBSBackupAttributes defaultInstance;
    public static MBSBackupAttributes getDefaultInstance() {
      return defaultInstance;
    }

    public MBSBackupAttributes getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private MBSBackupAttributes(
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
              deviceClass_ = bs;
              break;
            }
            case 18: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000002;
              productType_ = bs;
              break;
            }
            case 26: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000004;
              serialNumber_ = bs;
              break;
            }
            case 34: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000008;
              deviceColor_ = bs;
              break;
            }
            case 42: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000010;
              hardwareModel_ = bs;
              break;
            }
            case 50: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000020;
              marketingName_ = bs;
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
      return ICloud.internal_static_MBSBackupAttributes_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ICloud.internal_static_MBSBackupAttributes_fieldAccessorTable
          .ensureFieldAccessorsInitialized(ICloud.MBSBackupAttributes.class, ICloud.MBSBackupAttributes.Builder.class);
    }

    public static com.google.protobuf.Parser<MBSBackupAttributes> PARSER =
        new com.google.protobuf.AbstractParser<MBSBackupAttributes>() {
      public MBSBackupAttributes parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MBSBackupAttributes(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<MBSBackupAttributes> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int DEVICECLASS_FIELD_NUMBER = 1;
    private java.lang.Object deviceClass_;
    /**
     * <code>optional string DeviceClass = 1;</code>
     */
    public boolean hasDeviceClass() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional string DeviceClass = 1;</code>
     */
    public java.lang.String getDeviceClass() {
      java.lang.Object ref = deviceClass_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          deviceClass_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string DeviceClass = 1;</code>
     */
    public com.google.protobuf.ByteString
        getDeviceClassBytes() {
      java.lang.Object ref = deviceClass_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        deviceClass_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int PRODUCTTYPE_FIELD_NUMBER = 2;
    private java.lang.Object productType_;
    /**
     * <code>optional string ProductType = 2;</code>
     */
    public boolean hasProductType() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional string ProductType = 2;</code>
     */
    public java.lang.String getProductType() {
      java.lang.Object ref = productType_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          productType_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string ProductType = 2;</code>
     */
    public com.google.protobuf.ByteString
        getProductTypeBytes() {
      java.lang.Object ref = productType_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        productType_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int SERIALNUMBER_FIELD_NUMBER = 3;
    private java.lang.Object serialNumber_;
    /**
     * <code>optional string SerialNumber = 3;</code>
     */
    public boolean hasSerialNumber() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional string SerialNumber = 3;</code>
     */
    public java.lang.String getSerialNumber() {
      java.lang.Object ref = serialNumber_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          serialNumber_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string SerialNumber = 3;</code>
     */
    public com.google.protobuf.ByteString
        getSerialNumberBytes() {
      java.lang.Object ref = serialNumber_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        serialNumber_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int DEVICECOLOR_FIELD_NUMBER = 4;
    private java.lang.Object deviceColor_;
    /**
     * <code>optional string DeviceColor = 4;</code>
     */
    public boolean hasDeviceColor() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional string DeviceColor = 4;</code>
     */
    public java.lang.String getDeviceColor() {
      java.lang.Object ref = deviceColor_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          deviceColor_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string DeviceColor = 4;</code>
     */
    public com.google.protobuf.ByteString
        getDeviceColorBytes() {
      java.lang.Object ref = deviceColor_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        deviceColor_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int HARDWAREMODEL_FIELD_NUMBER = 5;
    private java.lang.Object hardwareModel_;
    /**
     * <code>optional string HardwareModel = 5;</code>
     */
    public boolean hasHardwareModel() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    /**
     * <code>optional string HardwareModel = 5;</code>
     */
    public java.lang.String getHardwareModel() {
      java.lang.Object ref = hardwareModel_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          hardwareModel_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string HardwareModel = 5;</code>
     */
    public com.google.protobuf.ByteString
        getHardwareModelBytes() {
      java.lang.Object ref = hardwareModel_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        hardwareModel_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int MARKETINGNAME_FIELD_NUMBER = 6;
    private java.lang.Object marketingName_;
    /**
     * <code>optional string MarketingName = 6;</code>
     */
    public boolean hasMarketingName() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    /**
     * <code>optional string MarketingName = 6;</code>
     */
    public java.lang.String getMarketingName() {
      java.lang.Object ref = marketingName_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          marketingName_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string MarketingName = 6;</code>
     */
    public com.google.protobuf.ByteString
        getMarketingNameBytes() {
      java.lang.Object ref = marketingName_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        marketingName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private void initFields() {
      deviceClass_ = "";
      productType_ = "";
      serialNumber_ = "";
      deviceColor_ = "";
      hardwareModel_ = "";
      marketingName_ = "";
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
        output.writeBytes(1, getDeviceClassBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getProductTypeBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, getSerialNumberBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBytes(4, getDeviceColorBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(5, getHardwareModelBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeBytes(6, getMarketingNameBytes());
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
          .computeBytesSize(1, getDeviceClassBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getProductTypeBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getSerialNumberBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(4, getDeviceColorBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(5, getHardwareModelBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(6, getMarketingNameBytes());
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

    public static ICloud.MBSBackupAttributes parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSBackupAttributes parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSBackupAttributes parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSBackupAttributes parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSBackupAttributes parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSBackupAttributes parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static ICloud.MBSBackupAttributes parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static ICloud.MBSBackupAttributes parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static ICloud.MBSBackupAttributes parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSBackupAttributes parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(ICloud.MBSBackupAttributes prototype) {
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
     * Protobuf type {@code MBSBackupAttributes}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:MBSBackupAttributes)
        ICloud.MBSBackupAttributesOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ICloud.internal_static_MBSBackupAttributes_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ICloud.internal_static_MBSBackupAttributes_fieldAccessorTable
            .ensureFieldAccessorsInitialized(ICloud.MBSBackupAttributes.class, ICloud.MBSBackupAttributes.Builder.class);
      }

      // Construct using Icloud.MBSBackupAttributes.newBuilder()
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
        deviceClass_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        productType_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        serialNumber_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        deviceColor_ = "";
        bitField0_ = (bitField0_ & ~0x00000008);
        hardwareModel_ = "";
        bitField0_ = (bitField0_ & ~0x00000010);
        marketingName_ = "";
        bitField0_ = (bitField0_ & ~0x00000020);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ICloud.internal_static_MBSBackupAttributes_descriptor;
      }

      public ICloud.MBSBackupAttributes getDefaultInstanceForType() {
        return ICloud.MBSBackupAttributes.getDefaultInstance();
      }

      public ICloud.MBSBackupAttributes build() {
        ICloud.MBSBackupAttributes result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ICloud.MBSBackupAttributes buildPartial() {
        ICloud.MBSBackupAttributes result = new ICloud.MBSBackupAttributes(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.deviceClass_ = deviceClass_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.productType_ = productType_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.serialNumber_ = serialNumber_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.deviceColor_ = deviceColor_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.hardwareModel_ = hardwareModel_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.marketingName_ = marketingName_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ICloud.MBSBackupAttributes) {
          return mergeFrom((ICloud.MBSBackupAttributes)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ICloud.MBSBackupAttributes other) {
        if (other == ICloud.MBSBackupAttributes.getDefaultInstance()) return this;
        if (other.hasDeviceClass()) {
          bitField0_ |= 0x00000001;
          deviceClass_ = other.deviceClass_;
          onChanged();
        }
        if (other.hasProductType()) {
          bitField0_ |= 0x00000002;
          productType_ = other.productType_;
          onChanged();
        }
        if (other.hasSerialNumber()) {
          bitField0_ |= 0x00000004;
          serialNumber_ = other.serialNumber_;
          onChanged();
        }
        if (other.hasDeviceColor()) {
          bitField0_ |= 0x00000008;
          deviceColor_ = other.deviceColor_;
          onChanged();
        }
        if (other.hasHardwareModel()) {
          bitField0_ |= 0x00000010;
          hardwareModel_ = other.hardwareModel_;
          onChanged();
        }
        if (other.hasMarketingName()) {
          bitField0_ |= 0x00000020;
          marketingName_ = other.marketingName_;
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
        ICloud.MBSBackupAttributes parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ICloud.MBSBackupAttributes) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object deviceClass_ = "";
      /**
       * <code>optional string DeviceClass = 1;</code>
       */
      public boolean hasDeviceClass() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional string DeviceClass = 1;</code>
       */
      public java.lang.String getDeviceClass() {
        java.lang.Object ref = deviceClass_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            deviceClass_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string DeviceClass = 1;</code>
       */
      public com.google.protobuf.ByteString
          getDeviceClassBytes() {
        java.lang.Object ref = deviceClass_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          deviceClass_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string DeviceClass = 1;</code>
       */
      public Builder setDeviceClass(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        deviceClass_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string DeviceClass = 1;</code>
       */
      public Builder clearDeviceClass() {
        bitField0_ = (bitField0_ & ~0x00000001);
        deviceClass_ = getDefaultInstance().getDeviceClass();
        onChanged();
        return this;
      }
      /**
       * <code>optional string DeviceClass = 1;</code>
       */
      public Builder setDeviceClassBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        deviceClass_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object productType_ = "";
      /**
       * <code>optional string ProductType = 2;</code>
       */
      public boolean hasProductType() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional string ProductType = 2;</code>
       */
      public java.lang.String getProductType() {
        java.lang.Object ref = productType_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            productType_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string ProductType = 2;</code>
       */
      public com.google.protobuf.ByteString
          getProductTypeBytes() {
        java.lang.Object ref = productType_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          productType_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string ProductType = 2;</code>
       */
      public Builder setProductType(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        productType_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string ProductType = 2;</code>
       */
      public Builder clearProductType() {
        bitField0_ = (bitField0_ & ~0x00000002);
        productType_ = getDefaultInstance().getProductType();
        onChanged();
        return this;
      }
      /**
       * <code>optional string ProductType = 2;</code>
       */
      public Builder setProductTypeBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        productType_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object serialNumber_ = "";
      /**
       * <code>optional string SerialNumber = 3;</code>
       */
      public boolean hasSerialNumber() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional string SerialNumber = 3;</code>
       */
      public java.lang.String getSerialNumber() {
        java.lang.Object ref = serialNumber_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            serialNumber_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string SerialNumber = 3;</code>
       */
      public com.google.protobuf.ByteString
          getSerialNumberBytes() {
        java.lang.Object ref = serialNumber_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          serialNumber_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string SerialNumber = 3;</code>
       */
      public Builder setSerialNumber(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        serialNumber_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string SerialNumber = 3;</code>
       */
      public Builder clearSerialNumber() {
        bitField0_ = (bitField0_ & ~0x00000004);
        serialNumber_ = getDefaultInstance().getSerialNumber();
        onChanged();
        return this;
      }
      /**
       * <code>optional string SerialNumber = 3;</code>
       */
      public Builder setSerialNumberBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        serialNumber_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object deviceColor_ = "";
      /**
       * <code>optional string DeviceColor = 4;</code>
       */
      public boolean hasDeviceColor() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>optional string DeviceColor = 4;</code>
       */
      public java.lang.String getDeviceColor() {
        java.lang.Object ref = deviceColor_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            deviceColor_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string DeviceColor = 4;</code>
       */
      public com.google.protobuf.ByteString
          getDeviceColorBytes() {
        java.lang.Object ref = deviceColor_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          deviceColor_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string DeviceColor = 4;</code>
       */
      public Builder setDeviceColor(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        deviceColor_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string DeviceColor = 4;</code>
       */
      public Builder clearDeviceColor() {
        bitField0_ = (bitField0_ & ~0x00000008);
        deviceColor_ = getDefaultInstance().getDeviceColor();
        onChanged();
        return this;
      }
      /**
       * <code>optional string DeviceColor = 4;</code>
       */
      public Builder setDeviceColorBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        deviceColor_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object hardwareModel_ = "";
      /**
       * <code>optional string HardwareModel = 5;</code>
       */
      public boolean hasHardwareModel() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      /**
       * <code>optional string HardwareModel = 5;</code>
       */
      public java.lang.String getHardwareModel() {
        java.lang.Object ref = hardwareModel_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            hardwareModel_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string HardwareModel = 5;</code>
       */
      public com.google.protobuf.ByteString
          getHardwareModelBytes() {
        java.lang.Object ref = hardwareModel_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          hardwareModel_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string HardwareModel = 5;</code>
       */
      public Builder setHardwareModel(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        hardwareModel_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string HardwareModel = 5;</code>
       */
      public Builder clearHardwareModel() {
        bitField0_ = (bitField0_ & ~0x00000010);
        hardwareModel_ = getDefaultInstance().getHardwareModel();
        onChanged();
        return this;
      }
      /**
       * <code>optional string HardwareModel = 5;</code>
       */
      public Builder setHardwareModelBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        hardwareModel_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object marketingName_ = "";
      /**
       * <code>optional string MarketingName = 6;</code>
       */
      public boolean hasMarketingName() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      /**
       * <code>optional string MarketingName = 6;</code>
       */
      public java.lang.String getMarketingName() {
        java.lang.Object ref = marketingName_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            marketingName_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string MarketingName = 6;</code>
       */
      public com.google.protobuf.ByteString
          getMarketingNameBytes() {
        java.lang.Object ref = marketingName_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          marketingName_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string MarketingName = 6;</code>
       */
      public Builder setMarketingName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        marketingName_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string MarketingName = 6;</code>
       */
      public Builder clearMarketingName() {
        bitField0_ = (bitField0_ & ~0x00000020);
        marketingName_ = getDefaultInstance().getMarketingName();
        onChanged();
        return this;
      }
      /**
       * <code>optional string MarketingName = 6;</code>
       */
      public Builder setMarketingNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        marketingName_ = value;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:MBSBackupAttributes)
    }

    static {
      defaultInstance = new MBSBackupAttributes(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:MBSBackupAttributes)
  }

  public interface MBSFileOrBuilder extends
      // @@protoc_insertion_point(interface_extends:MBSFile)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional bytes FileID = 1;</code>
     */
    boolean hasFileID();
    /**
     * <code>optional bytes FileID = 1;</code>
     */
    com.google.protobuf.ByteString getFileID();

    /**
     * <code>optional string Domain = 2;</code>
     */
    boolean hasDomain();
    /**
     * <code>optional string Domain = 2;</code>
     */
    java.lang.String getDomain();
    /**
     * <code>optional string Domain = 2;</code>
     */
    com.google.protobuf.ByteString
        getDomainBytes();

    /**
     * <code>optional string RelativePath = 3;</code>
     */
    boolean hasRelativePath();
    /**
     * <code>optional string RelativePath = 3;</code>
     */
    java.lang.String getRelativePath();
    /**
     * <code>optional string RelativePath = 3;</code>
     */
    com.google.protobuf.ByteString
        getRelativePathBytes();

    /**
     * <code>optional bytes Signature = 4;</code>
     */
    boolean hasSignature();
    /**
     * <code>optional bytes Signature = 4;</code>
     */
    com.google.protobuf.ByteString getSignature();

    /**
     * <code>optional uint64 Size = 5;</code>
     */
    boolean hasSize();
    /**
     * <code>optional uint64 Size = 5;</code>
     */
    long getSize();

    /**
     * <code>optional .MBSFileAttributes Attributes = 6;</code>
     */
    boolean hasAttributes();
    /**
     * <code>optional .MBSFileAttributes Attributes = 6;</code>
     */
    ICloud.MBSFileAttributes getAttributes();
    /**
     * <code>optional .MBSFileAttributes Attributes = 6;</code>
     */
    ICloud.MBSFileAttributesOrBuilder getAttributesOrBuilder();
  }
  /**
   * Protobuf type {@code MBSFile}
   */
  public static final class MBSFile extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:MBSFile)
      MBSFileOrBuilder {
    // Use MBSFile.newBuilder() to construct.
    private MBSFile(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private MBSFile(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final MBSFile defaultInstance;
    public static MBSFile getDefaultInstance() {
      return defaultInstance;
    }

    public MBSFile getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private MBSFile(
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
              fileID_ = input.readBytes();
              break;
            }
            case 18: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000002;
              domain_ = bs;
              break;
            }
            case 26: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000004;
              relativePath_ = bs;
              break;
            }
            case 34: {
              bitField0_ |= 0x00000008;
              signature_ = input.readBytes();
              break;
            }
            case 40: {
              bitField0_ |= 0x00000010;
              size_ = input.readUInt64();
              break;
            }
            case 50: {
              ICloud.MBSFileAttributes.Builder subBuilder = null;
              if (((bitField0_ & 0x00000020) == 0x00000020)) {
                subBuilder = attributes_.toBuilder();
              }
              attributes_ = input.readMessage(ICloud.MBSFileAttributes.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(attributes_);
                attributes_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000020;
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
      return ICloud.internal_static_MBSFile_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ICloud.internal_static_MBSFile_fieldAccessorTable
          .ensureFieldAccessorsInitialized(ICloud.MBSFile.class, ICloud.MBSFile.Builder.class);
    }

    public static com.google.protobuf.Parser<MBSFile> PARSER =
        new com.google.protobuf.AbstractParser<MBSFile>() {
      public MBSFile parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MBSFile(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<MBSFile> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int FILEID_FIELD_NUMBER = 1;
    private com.google.protobuf.ByteString fileID_;
    /**
     * <code>optional bytes FileID = 1;</code>
     */
    public boolean hasFileID() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional bytes FileID = 1;</code>
     */
    public com.google.protobuf.ByteString getFileID() {
      return fileID_;
    }

    public static final int DOMAIN_FIELD_NUMBER = 2;
    private java.lang.Object domain_;
    /**
     * <code>optional string Domain = 2;</code>
     */
    public boolean hasDomain() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional string Domain = 2;</code>
     */
    public java.lang.String getDomain() {
      java.lang.Object ref = domain_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          domain_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string Domain = 2;</code>
     */
    public com.google.protobuf.ByteString
        getDomainBytes() {
      java.lang.Object ref = domain_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        domain_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int RELATIVEPATH_FIELD_NUMBER = 3;
    private java.lang.Object relativePath_;
    /**
     * <code>optional string RelativePath = 3;</code>
     */
    public boolean hasRelativePath() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional string RelativePath = 3;</code>
     */
    public java.lang.String getRelativePath() {
      java.lang.Object ref = relativePath_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          relativePath_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string RelativePath = 3;</code>
     */
    public com.google.protobuf.ByteString
        getRelativePathBytes() {
      java.lang.Object ref = relativePath_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        relativePath_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int SIGNATURE_FIELD_NUMBER = 4;
    private com.google.protobuf.ByteString signature_;
    /**
     * <code>optional bytes Signature = 4;</code>
     */
    public boolean hasSignature() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional bytes Signature = 4;</code>
     */
    public com.google.protobuf.ByteString getSignature() {
      return signature_;
    }

    public static final int SIZE_FIELD_NUMBER = 5;
    private long size_;
    /**
     * <code>optional uint64 Size = 5;</code>
     */
    public boolean hasSize() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    /**
     * <code>optional uint64 Size = 5;</code>
     */
    public long getSize() {
      return size_;
    }

    public static final int ATTRIBUTES_FIELD_NUMBER = 6;
    private ICloud.MBSFileAttributes attributes_;
    /**
     * <code>optional .MBSFileAttributes Attributes = 6;</code>
     */
    public boolean hasAttributes() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    /**
     * <code>optional .MBSFileAttributes Attributes = 6;</code>
     */
    public ICloud.MBSFileAttributes getAttributes() {
      return attributes_;
    }
    /**
     * <code>optional .MBSFileAttributes Attributes = 6;</code>
     */
    public ICloud.MBSFileAttributesOrBuilder getAttributesOrBuilder() {
      return attributes_;
    }

    private void initFields() {
      fileID_ = com.google.protobuf.ByteString.EMPTY;
      domain_ = "";
      relativePath_ = "";
      signature_ = com.google.protobuf.ByteString.EMPTY;
      size_ = 0L;
      attributes_ = ICloud.MBSFileAttributes.getDefaultInstance();
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
        output.writeBytes(1, fileID_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getDomainBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, getRelativePathBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBytes(4, signature_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeUInt64(5, size_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeMessage(6, attributes_);
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
          .computeBytesSize(1, fileID_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getDomainBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getRelativePathBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(4, signature_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(5, size_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(6, attributes_);
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

    public static ICloud.MBSFile parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSFile parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSFile parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSFile parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSFile parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSFile parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static ICloud.MBSFile parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static ICloud.MBSFile parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static ICloud.MBSFile parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSFile parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(ICloud.MBSFile prototype) {
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
     * Protobuf type {@code MBSFile}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:MBSFile)
        ICloud.MBSFileOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ICloud.internal_static_MBSFile_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ICloud.internal_static_MBSFile_fieldAccessorTable
            .ensureFieldAccessorsInitialized(ICloud.MBSFile.class, ICloud.MBSFile.Builder.class);
      }

      // Construct using Icloud.MBSFile.newBuilder()
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
          getAttributesFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        fileID_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        domain_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        relativePath_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        signature_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000008);
        size_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000010);
        if (attributesBuilder_ == null) {
          attributes_ = ICloud.MBSFileAttributes.getDefaultInstance();
        } else {
          attributesBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000020);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ICloud.internal_static_MBSFile_descriptor;
      }

      public ICloud.MBSFile getDefaultInstanceForType() {
        return ICloud.MBSFile.getDefaultInstance();
      }

      public ICloud.MBSFile build() {
        ICloud.MBSFile result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ICloud.MBSFile buildPartial() {
        ICloud.MBSFile result = new ICloud.MBSFile(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.fileID_ = fileID_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.domain_ = domain_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.relativePath_ = relativePath_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.signature_ = signature_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.size_ = size_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        if (attributesBuilder_ == null) {
          result.attributes_ = attributes_;
        } else {
          result.attributes_ = attributesBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ICloud.MBSFile) {
          return mergeFrom((ICloud.MBSFile)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ICloud.MBSFile other) {
        if (other == ICloud.MBSFile.getDefaultInstance()) return this;
        if (other.hasFileID()) {
          setFileID(other.getFileID());
        }
        if (other.hasDomain()) {
          bitField0_ |= 0x00000002;
          domain_ = other.domain_;
          onChanged();
        }
        if (other.hasRelativePath()) {
          bitField0_ |= 0x00000004;
          relativePath_ = other.relativePath_;
          onChanged();
        }
        if (other.hasSignature()) {
          setSignature(other.getSignature());
        }
        if (other.hasSize()) {
          setSize(other.getSize());
        }
        if (other.hasAttributes()) {
          mergeAttributes(other.getAttributes());
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
        ICloud.MBSFile parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ICloud.MBSFile) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.google.protobuf.ByteString fileID_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes FileID = 1;</code>
       */
      public boolean hasFileID() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional bytes FileID = 1;</code>
       */
      public com.google.protobuf.ByteString getFileID() {
        return fileID_;
      }
      /**
       * <code>optional bytes FileID = 1;</code>
       */
      public Builder setFileID(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        fileID_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes FileID = 1;</code>
       */
      public Builder clearFileID() {
        bitField0_ = (bitField0_ & ~0x00000001);
        fileID_ = getDefaultInstance().getFileID();
        onChanged();
        return this;
      }

      private java.lang.Object domain_ = "";
      /**
       * <code>optional string Domain = 2;</code>
       */
      public boolean hasDomain() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional string Domain = 2;</code>
       */
      public java.lang.String getDomain() {
        java.lang.Object ref = domain_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            domain_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string Domain = 2;</code>
       */
      public com.google.protobuf.ByteString
          getDomainBytes() {
        java.lang.Object ref = domain_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          domain_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string Domain = 2;</code>
       */
      public Builder setDomain(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        domain_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string Domain = 2;</code>
       */
      public Builder clearDomain() {
        bitField0_ = (bitField0_ & ~0x00000002);
        domain_ = getDefaultInstance().getDomain();
        onChanged();
        return this;
      }
      /**
       * <code>optional string Domain = 2;</code>
       */
      public Builder setDomainBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        domain_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object relativePath_ = "";
      /**
       * <code>optional string RelativePath = 3;</code>
       */
      public boolean hasRelativePath() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional string RelativePath = 3;</code>
       */
      public java.lang.String getRelativePath() {
        java.lang.Object ref = relativePath_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            relativePath_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string RelativePath = 3;</code>
       */
      public com.google.protobuf.ByteString
          getRelativePathBytes() {
        java.lang.Object ref = relativePath_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          relativePath_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string RelativePath = 3;</code>
       */
      public Builder setRelativePath(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        relativePath_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string RelativePath = 3;</code>
       */
      public Builder clearRelativePath() {
        bitField0_ = (bitField0_ & ~0x00000004);
        relativePath_ = getDefaultInstance().getRelativePath();
        onChanged();
        return this;
      }
      /**
       * <code>optional string RelativePath = 3;</code>
       */
      public Builder setRelativePathBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        relativePath_ = value;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString signature_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes Signature = 4;</code>
       */
      public boolean hasSignature() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>optional bytes Signature = 4;</code>
       */
      public com.google.protobuf.ByteString getSignature() {
        return signature_;
      }
      /**
       * <code>optional bytes Signature = 4;</code>
       */
      public Builder setSignature(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        signature_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes Signature = 4;</code>
       */
      public Builder clearSignature() {
        bitField0_ = (bitField0_ & ~0x00000008);
        signature_ = getDefaultInstance().getSignature();
        onChanged();
        return this;
      }

      private long size_ ;
      /**
       * <code>optional uint64 Size = 5;</code>
       */
      public boolean hasSize() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      /**
       * <code>optional uint64 Size = 5;</code>
       */
      public long getSize() {
        return size_;
      }
      /**
       * <code>optional uint64 Size = 5;</code>
       */
      public Builder setSize(long value) {
        bitField0_ |= 0x00000010;
        size_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint64 Size = 5;</code>
       */
      public Builder clearSize() {
        bitField0_ = (bitField0_ & ~0x00000010);
        size_ = 0L;
        onChanged();
        return this;
      }

      private ICloud.MBSFileAttributes attributes_ = ICloud.MBSFileAttributes.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          ICloud.MBSFileAttributes, ICloud.MBSFileAttributes.Builder, ICloud.MBSFileAttributesOrBuilder> attributesBuilder_;
      /**
       * <code>optional .MBSFileAttributes Attributes = 6;</code>
       */
      public boolean hasAttributes() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      /**
       * <code>optional .MBSFileAttributes Attributes = 6;</code>
       */
      public ICloud.MBSFileAttributes getAttributes() {
        if (attributesBuilder_ == null) {
          return attributes_;
        } else {
          return attributesBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .MBSFileAttributes Attributes = 6;</code>
       */
      public Builder setAttributes(ICloud.MBSFileAttributes value) {
        if (attributesBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          attributes_ = value;
          onChanged();
        } else {
          attributesBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000020;
        return this;
      }
      /**
       * <code>optional .MBSFileAttributes Attributes = 6;</code>
       */
      public Builder setAttributes(
          ICloud.MBSFileAttributes.Builder builderForValue) {
        if (attributesBuilder_ == null) {
          attributes_ = builderForValue.build();
          onChanged();
        } else {
          attributesBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000020;
        return this;
      }
      /**
       * <code>optional .MBSFileAttributes Attributes = 6;</code>
       */
      public Builder mergeAttributes(ICloud.MBSFileAttributes value) {
        if (attributesBuilder_ == null) {
          if (((bitField0_ & 0x00000020) == 0x00000020) &&
              attributes_ != ICloud.MBSFileAttributes.getDefaultInstance()) {
            attributes_ =
              ICloud.MBSFileAttributes.newBuilder(attributes_).mergeFrom(value).buildPartial();
          } else {
            attributes_ = value;
          }
          onChanged();
        } else {
          attributesBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000020;
        return this;
      }
      /**
       * <code>optional .MBSFileAttributes Attributes = 6;</code>
       */
      public Builder clearAttributes() {
        if (attributesBuilder_ == null) {
          attributes_ = ICloud.MBSFileAttributes.getDefaultInstance();
          onChanged();
        } else {
          attributesBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000020);
        return this;
      }
      /**
       * <code>optional .MBSFileAttributes Attributes = 6;</code>
       */
      public ICloud.MBSFileAttributes.Builder getAttributesBuilder() {
        bitField0_ |= 0x00000020;
        onChanged();
        return getAttributesFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .MBSFileAttributes Attributes = 6;</code>
       */
      public ICloud.MBSFileAttributesOrBuilder getAttributesOrBuilder() {
        if (attributesBuilder_ != null) {
          return attributesBuilder_.getMessageOrBuilder();
        } else {
          return attributes_;
        }
      }
      /**
       * <code>optional .MBSFileAttributes Attributes = 6;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          ICloud.MBSFileAttributes, ICloud.MBSFileAttributes.Builder, ICloud.MBSFileAttributesOrBuilder> 
          getAttributesFieldBuilder() {
        if (attributesBuilder_ == null) {
          attributesBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              ICloud.MBSFileAttributes, ICloud.MBSFileAttributes.Builder, ICloud.MBSFileAttributesOrBuilder>(
                  getAttributes(),
                  getParentForChildren(),
                  isClean());
          attributes_ = null;
        }
        return attributesBuilder_;
      }

      // @@protoc_insertion_point(builder_scope:MBSFile)
    }

    static {
      defaultInstance = new MBSFile(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:MBSFile)
  }

  public interface MBSFileAttributesOrBuilder extends
      // @@protoc_insertion_point(interface_extends:MBSFileAttributes)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional uint32 KeybagID = 1;</code>
     */
    boolean hasKeybagID();
    /**
     * <code>optional uint32 KeybagID = 1;</code>
     */
    int getKeybagID();

    /**
     * <code>optional string Target = 2;</code>
     */
    boolean hasTarget();
    /**
     * <code>optional string Target = 2;</code>
     */
    java.lang.String getTarget();
    /**
     * <code>optional string Target = 2;</code>
     */
    com.google.protobuf.ByteString
        getTargetBytes();

    /**
     * <code>optional bytes EncryptionKey = 3;</code>
     */
    boolean hasEncryptionKey();
    /**
     * <code>optional bytes EncryptionKey = 3;</code>
     */
    com.google.protobuf.ByteString getEncryptionKey();

    /**
     * <code>optional uint64 InodeNumber = 4;</code>
     */
    boolean hasInodeNumber();
    /**
     * <code>optional uint64 InodeNumber = 4;</code>
     */
    long getInodeNumber();

    /**
     * <code>optional uint32 Mode = 5;</code>
     */
    boolean hasMode();
    /**
     * <code>optional uint32 Mode = 5;</code>
     */
    int getMode();

    /**
     * <code>optional uint32 UserID = 6;</code>
     */
    boolean hasUserID();
    /**
     * <code>optional uint32 UserID = 6;</code>
     */
    int getUserID();

    /**
     * <code>optional uint32 GroupID = 7;</code>
     */
    boolean hasGroupID();
    /**
     * <code>optional uint32 GroupID = 7;</code>
     */
    int getGroupID();

    /**
     * <code>optional uint64 LastModified = 8;</code>
     */
    boolean hasLastModified();
    /**
     * <code>optional uint64 LastModified = 8;</code>
     */
    long getLastModified();

    /**
     * <code>optional uint64 LastStatusChange = 9;</code>
     */
    boolean hasLastStatusChange();
    /**
     * <code>optional uint64 LastStatusChange = 9;</code>
     */
    long getLastStatusChange();

    /**
     * <code>optional uint64 Birth = 10;</code>
     */
    boolean hasBirth();
    /**
     * <code>optional uint64 Birth = 10;</code>
     */
    long getBirth();

    /**
     * <code>optional uint32 ProtectionClass = 12;</code>
     */
    boolean hasProtectionClass();
    /**
     * <code>optional uint32 ProtectionClass = 12;</code>
     */
    int getProtectionClass();

    /**
     * <code>optional .MBSFileExtendedAttribute ExtendedAttribute = 13;</code>
     */
    boolean hasExtendedAttribute();
    /**
     * <code>optional .MBSFileExtendedAttribute ExtendedAttribute = 13;</code>
     */
    ICloud.MBSFileExtendedAttribute getExtendedAttribute();
    /**
     * <code>optional .MBSFileExtendedAttribute ExtendedAttribute = 13;</code>
     */
    ICloud.MBSFileExtendedAttributeOrBuilder getExtendedAttributeOrBuilder();

    /**
     * <code>optional uint32 EncryptionKeyVersion = 14;</code>
     */
    boolean hasEncryptionKeyVersion();
    /**
     * <code>optional uint32 EncryptionKeyVersion = 14;</code>
     */
    int getEncryptionKeyVersion();

    /**
     * <code>optional uint64 DecryptedSize = 15;</code>
     */
    boolean hasDecryptedSize();
    /**
     * <code>optional uint64 DecryptedSize = 15;</code>
     */
    long getDecryptedSize();
  }
  /**
   * Protobuf type {@code MBSFileAttributes}
   */
  public static final class MBSFileAttributes extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:MBSFileAttributes)
      MBSFileAttributesOrBuilder {
    // Use MBSFileAttributes.newBuilder() to construct.
    private MBSFileAttributes(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private MBSFileAttributes(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final MBSFileAttributes defaultInstance;
    public static MBSFileAttributes getDefaultInstance() {
      return defaultInstance;
    }

    public MBSFileAttributes getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private MBSFileAttributes(
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
              keybagID_ = input.readUInt32();
              break;
            }
            case 18: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000002;
              target_ = bs;
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              encryptionKey_ = input.readBytes();
              break;
            }
            case 32: {
              bitField0_ |= 0x00000008;
              inodeNumber_ = input.readUInt64();
              break;
            }
            case 40: {
              bitField0_ |= 0x00000010;
              mode_ = input.readUInt32();
              break;
            }
            case 48: {
              bitField0_ |= 0x00000020;
              userID_ = input.readUInt32();
              break;
            }
            case 56: {
              bitField0_ |= 0x00000040;
              groupID_ = input.readUInt32();
              break;
            }
            case 64: {
              bitField0_ |= 0x00000080;
              lastModified_ = input.readUInt64();
              break;
            }
            case 72: {
              bitField0_ |= 0x00000100;
              lastStatusChange_ = input.readUInt64();
              break;
            }
            case 80: {
              bitField0_ |= 0x00000200;
              birth_ = input.readUInt64();
              break;
            }
            case 96: {
              bitField0_ |= 0x00000400;
              protectionClass_ = input.readUInt32();
              break;
            }
            case 106: {
              ICloud.MBSFileExtendedAttribute.Builder subBuilder = null;
              if (((bitField0_ & 0x00000800) == 0x00000800)) {
                subBuilder = extendedAttribute_.toBuilder();
              }
              extendedAttribute_ = input.readMessage(ICloud.MBSFileExtendedAttribute.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(extendedAttribute_);
                extendedAttribute_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000800;
              break;
            }
            case 112: {
              bitField0_ |= 0x00001000;
              encryptionKeyVersion_ = input.readUInt32();
              break;
            }
            case 120: {
              bitField0_ |= 0x00002000;
              decryptedSize_ = input.readUInt64();
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
      return ICloud.internal_static_MBSFileAttributes_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ICloud.internal_static_MBSFileAttributes_fieldAccessorTable
          .ensureFieldAccessorsInitialized(ICloud.MBSFileAttributes.class, ICloud.MBSFileAttributes.Builder.class);
    }

    public static com.google.protobuf.Parser<MBSFileAttributes> PARSER =
        new com.google.protobuf.AbstractParser<MBSFileAttributes>() {
      public MBSFileAttributes parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MBSFileAttributes(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<MBSFileAttributes> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int KEYBAGID_FIELD_NUMBER = 1;
    private int keybagID_;
    /**
     * <code>optional uint32 KeybagID = 1;</code>
     */
    public boolean hasKeybagID() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional uint32 KeybagID = 1;</code>
     */
    public int getKeybagID() {
      return keybagID_;
    }

    public static final int TARGET_FIELD_NUMBER = 2;
    private java.lang.Object target_;
    /**
     * <code>optional string Target = 2;</code>
     */
    public boolean hasTarget() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional string Target = 2;</code>
     */
    public java.lang.String getTarget() {
      java.lang.Object ref = target_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          target_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string Target = 2;</code>
     */
    public com.google.protobuf.ByteString
        getTargetBytes() {
      java.lang.Object ref = target_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        target_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int ENCRYPTIONKEY_FIELD_NUMBER = 3;
    private com.google.protobuf.ByteString encryptionKey_;
    /**
     * <code>optional bytes EncryptionKey = 3;</code>
     */
    public boolean hasEncryptionKey() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional bytes EncryptionKey = 3;</code>
     */
    public com.google.protobuf.ByteString getEncryptionKey() {
      return encryptionKey_;
    }

    public static final int INODENUMBER_FIELD_NUMBER = 4;
    private long inodeNumber_;
    /**
     * <code>optional uint64 InodeNumber = 4;</code>
     */
    public boolean hasInodeNumber() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional uint64 InodeNumber = 4;</code>
     */
    public long getInodeNumber() {
      return inodeNumber_;
    }

    public static final int MODE_FIELD_NUMBER = 5;
    private int mode_;
    /**
     * <code>optional uint32 Mode = 5;</code>
     */
    public boolean hasMode() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    /**
     * <code>optional uint32 Mode = 5;</code>
     */
    public int getMode() {
      return mode_;
    }

    public static final int USERID_FIELD_NUMBER = 6;
    private int userID_;
    /**
     * <code>optional uint32 UserID = 6;</code>
     */
    public boolean hasUserID() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    /**
     * <code>optional uint32 UserID = 6;</code>
     */
    public int getUserID() {
      return userID_;
    }

    public static final int GROUPID_FIELD_NUMBER = 7;
    private int groupID_;
    /**
     * <code>optional uint32 GroupID = 7;</code>
     */
    public boolean hasGroupID() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }
    /**
     * <code>optional uint32 GroupID = 7;</code>
     */
    public int getGroupID() {
      return groupID_;
    }

    public static final int LASTMODIFIED_FIELD_NUMBER = 8;
    private long lastModified_;
    /**
     * <code>optional uint64 LastModified = 8;</code>
     */
    public boolean hasLastModified() {
      return ((bitField0_ & 0x00000080) == 0x00000080);
    }
    /**
     * <code>optional uint64 LastModified = 8;</code>
     */
    public long getLastModified() {
      return lastModified_;
    }

    public static final int LASTSTATUSCHANGE_FIELD_NUMBER = 9;
    private long lastStatusChange_;
    /**
     * <code>optional uint64 LastStatusChange = 9;</code>
     */
    public boolean hasLastStatusChange() {
      return ((bitField0_ & 0x00000100) == 0x00000100);
    }
    /**
     * <code>optional uint64 LastStatusChange = 9;</code>
     */
    public long getLastStatusChange() {
      return lastStatusChange_;
    }

    public static final int BIRTH_FIELD_NUMBER = 10;
    private long birth_;
    /**
     * <code>optional uint64 Birth = 10;</code>
     */
    public boolean hasBirth() {
      return ((bitField0_ & 0x00000200) == 0x00000200);
    }
    /**
     * <code>optional uint64 Birth = 10;</code>
     */
    public long getBirth() {
      return birth_;
    }

    public static final int PROTECTIONCLASS_FIELD_NUMBER = 12;
    private int protectionClass_;
    /**
     * <code>optional uint32 ProtectionClass = 12;</code>
     */
    public boolean hasProtectionClass() {
      return ((bitField0_ & 0x00000400) == 0x00000400);
    }
    /**
     * <code>optional uint32 ProtectionClass = 12;</code>
     */
    public int getProtectionClass() {
      return protectionClass_;
    }

    public static final int EXTENDEDATTRIBUTE_FIELD_NUMBER = 13;
    private ICloud.MBSFileExtendedAttribute extendedAttribute_;
    /**
     * <code>optional .MBSFileExtendedAttribute ExtendedAttribute = 13;</code>
     */
    public boolean hasExtendedAttribute() {
      return ((bitField0_ & 0x00000800) == 0x00000800);
    }
    /**
     * <code>optional .MBSFileExtendedAttribute ExtendedAttribute = 13;</code>
     */
    public ICloud.MBSFileExtendedAttribute getExtendedAttribute() {
      return extendedAttribute_;
    }
    /**
     * <code>optional .MBSFileExtendedAttribute ExtendedAttribute = 13;</code>
     */
    public ICloud.MBSFileExtendedAttributeOrBuilder getExtendedAttributeOrBuilder() {
      return extendedAttribute_;
    }

    public static final int ENCRYPTIONKEYVERSION_FIELD_NUMBER = 14;
    private int encryptionKeyVersion_;
    /**
     * <code>optional uint32 EncryptionKeyVersion = 14;</code>
     */
    public boolean hasEncryptionKeyVersion() {
      return ((bitField0_ & 0x00001000) == 0x00001000);
    }
    /**
     * <code>optional uint32 EncryptionKeyVersion = 14;</code>
     */
    public int getEncryptionKeyVersion() {
      return encryptionKeyVersion_;
    }

    public static final int DECRYPTEDSIZE_FIELD_NUMBER = 15;
    private long decryptedSize_;
    /**
     * <code>optional uint64 DecryptedSize = 15;</code>
     */
    public boolean hasDecryptedSize() {
      return ((bitField0_ & 0x00002000) == 0x00002000);
    }
    /**
     * <code>optional uint64 DecryptedSize = 15;</code>
     */
    public long getDecryptedSize() {
      return decryptedSize_;
    }

    private void initFields() {
      keybagID_ = 0;
      target_ = "";
      encryptionKey_ = com.google.protobuf.ByteString.EMPTY;
      inodeNumber_ = 0L;
      mode_ = 0;
      userID_ = 0;
      groupID_ = 0;
      lastModified_ = 0L;
      lastStatusChange_ = 0L;
      birth_ = 0L;
      protectionClass_ = 0;
      extendedAttribute_ = ICloud.MBSFileExtendedAttribute.getDefaultInstance();
      encryptionKeyVersion_ = 0;
      decryptedSize_ = 0L;
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
        output.writeUInt32(1, keybagID_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getTargetBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, encryptionKey_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeUInt64(4, inodeNumber_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeUInt32(5, mode_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeUInt32(6, userID_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeUInt32(7, groupID_);
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        output.writeUInt64(8, lastModified_);
      }
      if (((bitField0_ & 0x00000100) == 0x00000100)) {
        output.writeUInt64(9, lastStatusChange_);
      }
      if (((bitField0_ & 0x00000200) == 0x00000200)) {
        output.writeUInt64(10, birth_);
      }
      if (((bitField0_ & 0x00000400) == 0x00000400)) {
        output.writeUInt32(12, protectionClass_);
      }
      if (((bitField0_ & 0x00000800) == 0x00000800)) {
        output.writeMessage(13, extendedAttribute_);
      }
      if (((bitField0_ & 0x00001000) == 0x00001000)) {
        output.writeUInt32(14, encryptionKeyVersion_);
      }
      if (((bitField0_ & 0x00002000) == 0x00002000)) {
        output.writeUInt64(15, decryptedSize_);
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
          .computeUInt32Size(1, keybagID_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getTargetBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, encryptionKey_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(4, inodeNumber_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(5, mode_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(6, userID_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(7, groupID_);
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(8, lastModified_);
      }
      if (((bitField0_ & 0x00000100) == 0x00000100)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(9, lastStatusChange_);
      }
      if (((bitField0_ & 0x00000200) == 0x00000200)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(10, birth_);
      }
      if (((bitField0_ & 0x00000400) == 0x00000400)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(12, protectionClass_);
      }
      if (((bitField0_ & 0x00000800) == 0x00000800)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(13, extendedAttribute_);
      }
      if (((bitField0_ & 0x00001000) == 0x00001000)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(14, encryptionKeyVersion_);
      }
      if (((bitField0_ & 0x00002000) == 0x00002000)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(15, decryptedSize_);
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

    public static ICloud.MBSFileAttributes parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSFileAttributes parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSFileAttributes parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSFileAttributes parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSFileAttributes parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSFileAttributes parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static ICloud.MBSFileAttributes parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static ICloud.MBSFileAttributes parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static ICloud.MBSFileAttributes parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSFileAttributes parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(ICloud.MBSFileAttributes prototype) {
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
     * Protobuf type {@code MBSFileAttributes}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:MBSFileAttributes)
        ICloud.MBSFileAttributesOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ICloud.internal_static_MBSFileAttributes_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ICloud.internal_static_MBSFileAttributes_fieldAccessorTable
            .ensureFieldAccessorsInitialized(ICloud.MBSFileAttributes.class, ICloud.MBSFileAttributes.Builder.class);
      }

      // Construct using Icloud.MBSFileAttributes.newBuilder()
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
          getExtendedAttributeFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        keybagID_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        target_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        encryptionKey_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000004);
        inodeNumber_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000008);
        mode_ = 0;
        bitField0_ = (bitField0_ & ~0x00000010);
        userID_ = 0;
        bitField0_ = (bitField0_ & ~0x00000020);
        groupID_ = 0;
        bitField0_ = (bitField0_ & ~0x00000040);
        lastModified_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000080);
        lastStatusChange_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000100);
        birth_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000200);
        protectionClass_ = 0;
        bitField0_ = (bitField0_ & ~0x00000400);
        if (extendedAttributeBuilder_ == null) {
          extendedAttribute_ = ICloud.MBSFileExtendedAttribute.getDefaultInstance();
        } else {
          extendedAttributeBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000800);
        encryptionKeyVersion_ = 0;
        bitField0_ = (bitField0_ & ~0x00001000);
        decryptedSize_ = 0L;
        bitField0_ = (bitField0_ & ~0x00002000);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ICloud.internal_static_MBSFileAttributes_descriptor;
      }

      public ICloud.MBSFileAttributes getDefaultInstanceForType() {
        return ICloud.MBSFileAttributes.getDefaultInstance();
      }

      public ICloud.MBSFileAttributes build() {
        ICloud.MBSFileAttributes result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ICloud.MBSFileAttributes buildPartial() {
        ICloud.MBSFileAttributes result = new ICloud.MBSFileAttributes(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.keybagID_ = keybagID_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.target_ = target_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.encryptionKey_ = encryptionKey_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.inodeNumber_ = inodeNumber_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.mode_ = mode_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.userID_ = userID_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000040;
        }
        result.groupID_ = groupID_;
        if (((from_bitField0_ & 0x00000080) == 0x00000080)) {
          to_bitField0_ |= 0x00000080;
        }
        result.lastModified_ = lastModified_;
        if (((from_bitField0_ & 0x00000100) == 0x00000100)) {
          to_bitField0_ |= 0x00000100;
        }
        result.lastStatusChange_ = lastStatusChange_;
        if (((from_bitField0_ & 0x00000200) == 0x00000200)) {
          to_bitField0_ |= 0x00000200;
        }
        result.birth_ = birth_;
        if (((from_bitField0_ & 0x00000400) == 0x00000400)) {
          to_bitField0_ |= 0x00000400;
        }
        result.protectionClass_ = protectionClass_;
        if (((from_bitField0_ & 0x00000800) == 0x00000800)) {
          to_bitField0_ |= 0x00000800;
        }
        if (extendedAttributeBuilder_ == null) {
          result.extendedAttribute_ = extendedAttribute_;
        } else {
          result.extendedAttribute_ = extendedAttributeBuilder_.build();
        }
        if (((from_bitField0_ & 0x00001000) == 0x00001000)) {
          to_bitField0_ |= 0x00001000;
        }
        result.encryptionKeyVersion_ = encryptionKeyVersion_;
        if (((from_bitField0_ & 0x00002000) == 0x00002000)) {
          to_bitField0_ |= 0x00002000;
        }
        result.decryptedSize_ = decryptedSize_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ICloud.MBSFileAttributes) {
          return mergeFrom((ICloud.MBSFileAttributes)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ICloud.MBSFileAttributes other) {
        if (other == ICloud.MBSFileAttributes.getDefaultInstance()) return this;
        if (other.hasKeybagID()) {
          setKeybagID(other.getKeybagID());
        }
        if (other.hasTarget()) {
          bitField0_ |= 0x00000002;
          target_ = other.target_;
          onChanged();
        }
        if (other.hasEncryptionKey()) {
          setEncryptionKey(other.getEncryptionKey());
        }
        if (other.hasInodeNumber()) {
          setInodeNumber(other.getInodeNumber());
        }
        if (other.hasMode()) {
          setMode(other.getMode());
        }
        if (other.hasUserID()) {
          setUserID(other.getUserID());
        }
        if (other.hasGroupID()) {
          setGroupID(other.getGroupID());
        }
        if (other.hasLastModified()) {
          setLastModified(other.getLastModified());
        }
        if (other.hasLastStatusChange()) {
          setLastStatusChange(other.getLastStatusChange());
        }
        if (other.hasBirth()) {
          setBirth(other.getBirth());
        }
        if (other.hasProtectionClass()) {
          setProtectionClass(other.getProtectionClass());
        }
        if (other.hasExtendedAttribute()) {
          mergeExtendedAttribute(other.getExtendedAttribute());
        }
        if (other.hasEncryptionKeyVersion()) {
          setEncryptionKeyVersion(other.getEncryptionKeyVersion());
        }
        if (other.hasDecryptedSize()) {
          setDecryptedSize(other.getDecryptedSize());
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
        ICloud.MBSFileAttributes parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ICloud.MBSFileAttributes) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int keybagID_ ;
      /**
       * <code>optional uint32 KeybagID = 1;</code>
       */
      public boolean hasKeybagID() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional uint32 KeybagID = 1;</code>
       */
      public int getKeybagID() {
        return keybagID_;
      }
      /**
       * <code>optional uint32 KeybagID = 1;</code>
       */
      public Builder setKeybagID(int value) {
        bitField0_ |= 0x00000001;
        keybagID_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 KeybagID = 1;</code>
       */
      public Builder clearKeybagID() {
        bitField0_ = (bitField0_ & ~0x00000001);
        keybagID_ = 0;
        onChanged();
        return this;
      }

      private java.lang.Object target_ = "";
      /**
       * <code>optional string Target = 2;</code>
       */
      public boolean hasTarget() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional string Target = 2;</code>
       */
      public java.lang.String getTarget() {
        java.lang.Object ref = target_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            target_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string Target = 2;</code>
       */
      public com.google.protobuf.ByteString
          getTargetBytes() {
        java.lang.Object ref = target_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          target_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string Target = 2;</code>
       */
      public Builder setTarget(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        target_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string Target = 2;</code>
       */
      public Builder clearTarget() {
        bitField0_ = (bitField0_ & ~0x00000002);
        target_ = getDefaultInstance().getTarget();
        onChanged();
        return this;
      }
      /**
       * <code>optional string Target = 2;</code>
       */
      public Builder setTargetBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        target_ = value;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString encryptionKey_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes EncryptionKey = 3;</code>
       */
      public boolean hasEncryptionKey() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional bytes EncryptionKey = 3;</code>
       */
      public com.google.protobuf.ByteString getEncryptionKey() {
        return encryptionKey_;
      }
      /**
       * <code>optional bytes EncryptionKey = 3;</code>
       */
      public Builder setEncryptionKey(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        encryptionKey_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes EncryptionKey = 3;</code>
       */
      public Builder clearEncryptionKey() {
        bitField0_ = (bitField0_ & ~0x00000004);
        encryptionKey_ = getDefaultInstance().getEncryptionKey();
        onChanged();
        return this;
      }

      private long inodeNumber_ ;
      /**
       * <code>optional uint64 InodeNumber = 4;</code>
       */
      public boolean hasInodeNumber() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>optional uint64 InodeNumber = 4;</code>
       */
      public long getInodeNumber() {
        return inodeNumber_;
      }
      /**
       * <code>optional uint64 InodeNumber = 4;</code>
       */
      public Builder setInodeNumber(long value) {
        bitField0_ |= 0x00000008;
        inodeNumber_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint64 InodeNumber = 4;</code>
       */
      public Builder clearInodeNumber() {
        bitField0_ = (bitField0_ & ~0x00000008);
        inodeNumber_ = 0L;
        onChanged();
        return this;
      }

      private int mode_ ;
      /**
       * <code>optional uint32 Mode = 5;</code>
       */
      public boolean hasMode() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      /**
       * <code>optional uint32 Mode = 5;</code>
       */
      public int getMode() {
        return mode_;
      }
      /**
       * <code>optional uint32 Mode = 5;</code>
       */
      public Builder setMode(int value) {
        bitField0_ |= 0x00000010;
        mode_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 Mode = 5;</code>
       */
      public Builder clearMode() {
        bitField0_ = (bitField0_ & ~0x00000010);
        mode_ = 0;
        onChanged();
        return this;
      }

      private int userID_ ;
      /**
       * <code>optional uint32 UserID = 6;</code>
       */
      public boolean hasUserID() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      /**
       * <code>optional uint32 UserID = 6;</code>
       */
      public int getUserID() {
        return userID_;
      }
      /**
       * <code>optional uint32 UserID = 6;</code>
       */
      public Builder setUserID(int value) {
        bitField0_ |= 0x00000020;
        userID_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 UserID = 6;</code>
       */
      public Builder clearUserID() {
        bitField0_ = (bitField0_ & ~0x00000020);
        userID_ = 0;
        onChanged();
        return this;
      }

      private int groupID_ ;
      /**
       * <code>optional uint32 GroupID = 7;</code>
       */
      public boolean hasGroupID() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }
      /**
       * <code>optional uint32 GroupID = 7;</code>
       */
      public int getGroupID() {
        return groupID_;
      }
      /**
       * <code>optional uint32 GroupID = 7;</code>
       */
      public Builder setGroupID(int value) {
        bitField0_ |= 0x00000040;
        groupID_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 GroupID = 7;</code>
       */
      public Builder clearGroupID() {
        bitField0_ = (bitField0_ & ~0x00000040);
        groupID_ = 0;
        onChanged();
        return this;
      }

      private long lastModified_ ;
      /**
       * <code>optional uint64 LastModified = 8;</code>
       */
      public boolean hasLastModified() {
        return ((bitField0_ & 0x00000080) == 0x00000080);
      }
      /**
       * <code>optional uint64 LastModified = 8;</code>
       */
      public long getLastModified() {
        return lastModified_;
      }
      /**
       * <code>optional uint64 LastModified = 8;</code>
       */
      public Builder setLastModified(long value) {
        bitField0_ |= 0x00000080;
        lastModified_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint64 LastModified = 8;</code>
       */
      public Builder clearLastModified() {
        bitField0_ = (bitField0_ & ~0x00000080);
        lastModified_ = 0L;
        onChanged();
        return this;
      }

      private long lastStatusChange_ ;
      /**
       * <code>optional uint64 LastStatusChange = 9;</code>
       */
      public boolean hasLastStatusChange() {
        return ((bitField0_ & 0x00000100) == 0x00000100);
      }
      /**
       * <code>optional uint64 LastStatusChange = 9;</code>
       */
      public long getLastStatusChange() {
        return lastStatusChange_;
      }
      /**
       * <code>optional uint64 LastStatusChange = 9;</code>
       */
      public Builder setLastStatusChange(long value) {
        bitField0_ |= 0x00000100;
        lastStatusChange_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint64 LastStatusChange = 9;</code>
       */
      public Builder clearLastStatusChange() {
        bitField0_ = (bitField0_ & ~0x00000100);
        lastStatusChange_ = 0L;
        onChanged();
        return this;
      }

      private long birth_ ;
      /**
       * <code>optional uint64 Birth = 10;</code>
       */
      public boolean hasBirth() {
        return ((bitField0_ & 0x00000200) == 0x00000200);
      }
      /**
       * <code>optional uint64 Birth = 10;</code>
       */
      public long getBirth() {
        return birth_;
      }
      /**
       * <code>optional uint64 Birth = 10;</code>
       */
      public Builder setBirth(long value) {
        bitField0_ |= 0x00000200;
        birth_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint64 Birth = 10;</code>
       */
      public Builder clearBirth() {
        bitField0_ = (bitField0_ & ~0x00000200);
        birth_ = 0L;
        onChanged();
        return this;
      }

      private int protectionClass_ ;
      /**
       * <code>optional uint32 ProtectionClass = 12;</code>
       */
      public boolean hasProtectionClass() {
        return ((bitField0_ & 0x00000400) == 0x00000400);
      }
      /**
       * <code>optional uint32 ProtectionClass = 12;</code>
       */
      public int getProtectionClass() {
        return protectionClass_;
      }
      /**
       * <code>optional uint32 ProtectionClass = 12;</code>
       */
      public Builder setProtectionClass(int value) {
        bitField0_ |= 0x00000400;
        protectionClass_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 ProtectionClass = 12;</code>
       */
      public Builder clearProtectionClass() {
        bitField0_ = (bitField0_ & ~0x00000400);
        protectionClass_ = 0;
        onChanged();
        return this;
      }

      private ICloud.MBSFileExtendedAttribute extendedAttribute_ = ICloud.MBSFileExtendedAttribute.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          ICloud.MBSFileExtendedAttribute, ICloud.MBSFileExtendedAttribute.Builder, ICloud.MBSFileExtendedAttributeOrBuilder> extendedAttributeBuilder_;
      /**
       * <code>optional .MBSFileExtendedAttribute ExtendedAttribute = 13;</code>
       */
      public boolean hasExtendedAttribute() {
        return ((bitField0_ & 0x00000800) == 0x00000800);
      }
      /**
       * <code>optional .MBSFileExtendedAttribute ExtendedAttribute = 13;</code>
       */
      public ICloud.MBSFileExtendedAttribute getExtendedAttribute() {
        if (extendedAttributeBuilder_ == null) {
          return extendedAttribute_;
        } else {
          return extendedAttributeBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .MBSFileExtendedAttribute ExtendedAttribute = 13;</code>
       */
      public Builder setExtendedAttribute(ICloud.MBSFileExtendedAttribute value) {
        if (extendedAttributeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          extendedAttribute_ = value;
          onChanged();
        } else {
          extendedAttributeBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000800;
        return this;
      }
      /**
       * <code>optional .MBSFileExtendedAttribute ExtendedAttribute = 13;</code>
       */
      public Builder setExtendedAttribute(
          ICloud.MBSFileExtendedAttribute.Builder builderForValue) {
        if (extendedAttributeBuilder_ == null) {
          extendedAttribute_ = builderForValue.build();
          onChanged();
        } else {
          extendedAttributeBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000800;
        return this;
      }
      /**
       * <code>optional .MBSFileExtendedAttribute ExtendedAttribute = 13;</code>
       */
      public Builder mergeExtendedAttribute(ICloud.MBSFileExtendedAttribute value) {
        if (extendedAttributeBuilder_ == null) {
          if (((bitField0_ & 0x00000800) == 0x00000800) &&
              extendedAttribute_ != ICloud.MBSFileExtendedAttribute.getDefaultInstance()) {
            extendedAttribute_ =
              ICloud.MBSFileExtendedAttribute.newBuilder(extendedAttribute_).mergeFrom(value).buildPartial();
          } else {
            extendedAttribute_ = value;
          }
          onChanged();
        } else {
          extendedAttributeBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000800;
        return this;
      }
      /**
       * <code>optional .MBSFileExtendedAttribute ExtendedAttribute = 13;</code>
       */
      public Builder clearExtendedAttribute() {
        if (extendedAttributeBuilder_ == null) {
          extendedAttribute_ = ICloud.MBSFileExtendedAttribute.getDefaultInstance();
          onChanged();
        } else {
          extendedAttributeBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000800);
        return this;
      }
      /**
       * <code>optional .MBSFileExtendedAttribute ExtendedAttribute = 13;</code>
       */
      public ICloud.MBSFileExtendedAttribute.Builder getExtendedAttributeBuilder() {
        bitField0_ |= 0x00000800;
        onChanged();
        return getExtendedAttributeFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .MBSFileExtendedAttribute ExtendedAttribute = 13;</code>
       */
      public ICloud.MBSFileExtendedAttributeOrBuilder getExtendedAttributeOrBuilder() {
        if (extendedAttributeBuilder_ != null) {
          return extendedAttributeBuilder_.getMessageOrBuilder();
        } else {
          return extendedAttribute_;
        }
      }
      /**
       * <code>optional .MBSFileExtendedAttribute ExtendedAttribute = 13;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          ICloud.MBSFileExtendedAttribute, ICloud.MBSFileExtendedAttribute.Builder, ICloud.MBSFileExtendedAttributeOrBuilder> 
          getExtendedAttributeFieldBuilder() {
        if (extendedAttributeBuilder_ == null) {
          extendedAttributeBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              ICloud.MBSFileExtendedAttribute, ICloud.MBSFileExtendedAttribute.Builder, ICloud.MBSFileExtendedAttributeOrBuilder>(
                  getExtendedAttribute(),
                  getParentForChildren(),
                  isClean());
          extendedAttribute_ = null;
        }
        return extendedAttributeBuilder_;
      }

      private int encryptionKeyVersion_ ;
      /**
       * <code>optional uint32 EncryptionKeyVersion = 14;</code>
       */
      public boolean hasEncryptionKeyVersion() {
        return ((bitField0_ & 0x00001000) == 0x00001000);
      }
      /**
       * <code>optional uint32 EncryptionKeyVersion = 14;</code>
       */
      public int getEncryptionKeyVersion() {
        return encryptionKeyVersion_;
      }
      /**
       * <code>optional uint32 EncryptionKeyVersion = 14;</code>
       */
      public Builder setEncryptionKeyVersion(int value) {
        bitField0_ |= 0x00001000;
        encryptionKeyVersion_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 EncryptionKeyVersion = 14;</code>
       */
      public Builder clearEncryptionKeyVersion() {
        bitField0_ = (bitField0_ & ~0x00001000);
        encryptionKeyVersion_ = 0;
        onChanged();
        return this;
      }

      private long decryptedSize_ ;
      /**
       * <code>optional uint64 DecryptedSize = 15;</code>
       */
      public boolean hasDecryptedSize() {
        return ((bitField0_ & 0x00002000) == 0x00002000);
      }
      /**
       * <code>optional uint64 DecryptedSize = 15;</code>
       */
      public long getDecryptedSize() {
        return decryptedSize_;
      }
      /**
       * <code>optional uint64 DecryptedSize = 15;</code>
       */
      public Builder setDecryptedSize(long value) {
        bitField0_ |= 0x00002000;
        decryptedSize_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint64 DecryptedSize = 15;</code>
       */
      public Builder clearDecryptedSize() {
        bitField0_ = (bitField0_ & ~0x00002000);
        decryptedSize_ = 0L;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:MBSFileAttributes)
    }

    static {
      defaultInstance = new MBSFileAttributes(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:MBSFileAttributes)
  }

  public interface MBSSnapshotOrBuilder extends
      // @@protoc_insertion_point(interface_extends:MBSSnapshot)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional uint32 SnapshotID = 1;</code>
     */
    boolean hasSnapshotID();
    /**
     * <code>optional uint32 SnapshotID = 1;</code>
     */
    int getSnapshotID();

    /**
     * <code>optional uint64 QuotaReserved = 2;</code>
     */
    boolean hasQuotaReserved();
    /**
     * <code>optional uint64 QuotaReserved = 2;</code>
     */
    long getQuotaReserved();

    /**
     * <code>optional uint64 LastModified = 3;</code>
     */
    boolean hasLastModified();
    /**
     * <code>optional uint64 LastModified = 3;</code>
     */
    long getLastModified();

    /**
     * <code>optional .MBSSnapshotAttributes Attributes = 5;</code>
     */
    boolean hasAttributes();
    /**
     * <code>optional .MBSSnapshotAttributes Attributes = 5;</code>
     */
    ICloud.MBSSnapshotAttributes getAttributes();
    /**
     * <code>optional .MBSSnapshotAttributes Attributes = 5;</code>
     */
    ICloud.MBSSnapshotAttributesOrBuilder getAttributesOrBuilder();

    /**
     * <code>optional uint64 Committed = 6;</code>
     */
    boolean hasCommitted();
    /**
     * <code>optional uint64 Committed = 6;</code>
     */
    long getCommitted();
  }
  /**
   * Protobuf type {@code MBSSnapshot}
   */
  public static final class MBSSnapshot extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:MBSSnapshot)
      MBSSnapshotOrBuilder {
    // Use MBSSnapshot.newBuilder() to construct.
    private MBSSnapshot(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private MBSSnapshot(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final MBSSnapshot defaultInstance;
    public static MBSSnapshot getDefaultInstance() {
      return defaultInstance;
    }

    public MBSSnapshot getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private MBSSnapshot(
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
              snapshotID_ = input.readUInt32();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              quotaReserved_ = input.readUInt64();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              lastModified_ = input.readUInt64();
              break;
            }
            case 42: {
              ICloud.MBSSnapshotAttributes.Builder subBuilder = null;
              if (((bitField0_ & 0x00000008) == 0x00000008)) {
                subBuilder = attributes_.toBuilder();
              }
              attributes_ = input.readMessage(ICloud.MBSSnapshotAttributes.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(attributes_);
                attributes_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000008;
              break;
            }
            case 48: {
              bitField0_ |= 0x00000010;
              committed_ = input.readUInt64();
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
      return ICloud.internal_static_MBSSnapshot_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ICloud.internal_static_MBSSnapshot_fieldAccessorTable
          .ensureFieldAccessorsInitialized(ICloud.MBSSnapshot.class, ICloud.MBSSnapshot.Builder.class);
    }

    public static com.google.protobuf.Parser<MBSSnapshot> PARSER =
        new com.google.protobuf.AbstractParser<MBSSnapshot>() {
      public MBSSnapshot parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MBSSnapshot(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<MBSSnapshot> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int SNAPSHOTID_FIELD_NUMBER = 1;
    private int snapshotID_;
    /**
     * <code>optional uint32 SnapshotID = 1;</code>
     */
    public boolean hasSnapshotID() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional uint32 SnapshotID = 1;</code>
     */
    public int getSnapshotID() {
      return snapshotID_;
    }

    public static final int QUOTARESERVED_FIELD_NUMBER = 2;
    private long quotaReserved_;
    /**
     * <code>optional uint64 QuotaReserved = 2;</code>
     */
    public boolean hasQuotaReserved() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional uint64 QuotaReserved = 2;</code>
     */
    public long getQuotaReserved() {
      return quotaReserved_;
    }

    public static final int LASTMODIFIED_FIELD_NUMBER = 3;
    private long lastModified_;
    /**
     * <code>optional uint64 LastModified = 3;</code>
     */
    public boolean hasLastModified() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional uint64 LastModified = 3;</code>
     */
    public long getLastModified() {
      return lastModified_;
    }

    public static final int ATTRIBUTES_FIELD_NUMBER = 5;
    private ICloud.MBSSnapshotAttributes attributes_;
    /**
     * <code>optional .MBSSnapshotAttributes Attributes = 5;</code>
     */
    public boolean hasAttributes() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional .MBSSnapshotAttributes Attributes = 5;</code>
     */
    public ICloud.MBSSnapshotAttributes getAttributes() {
      return attributes_;
    }
    /**
     * <code>optional .MBSSnapshotAttributes Attributes = 5;</code>
     */
    public ICloud.MBSSnapshotAttributesOrBuilder getAttributesOrBuilder() {
      return attributes_;
    }

    public static final int COMMITTED_FIELD_NUMBER = 6;
    private long committed_;
    /**
     * <code>optional uint64 Committed = 6;</code>
     */
    public boolean hasCommitted() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    /**
     * <code>optional uint64 Committed = 6;</code>
     */
    public long getCommitted() {
      return committed_;
    }

    private void initFields() {
      snapshotID_ = 0;
      quotaReserved_ = 0L;
      lastModified_ = 0L;
      attributes_ = ICloud.MBSSnapshotAttributes.getDefaultInstance();
      committed_ = 0L;
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
        output.writeUInt32(1, snapshotID_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeUInt64(2, quotaReserved_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeUInt64(3, lastModified_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeMessage(5, attributes_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeUInt64(6, committed_);
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
          .computeUInt32Size(1, snapshotID_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(2, quotaReserved_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(3, lastModified_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(5, attributes_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(6, committed_);
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

    public static ICloud.MBSSnapshot parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSSnapshot parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSSnapshot parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSSnapshot parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSSnapshot parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSSnapshot parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static ICloud.MBSSnapshot parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static ICloud.MBSSnapshot parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static ICloud.MBSSnapshot parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSSnapshot parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(ICloud.MBSSnapshot prototype) {
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
     * Protobuf type {@code MBSSnapshot}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:MBSSnapshot)
        ICloud.MBSSnapshotOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ICloud.internal_static_MBSSnapshot_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ICloud.internal_static_MBSSnapshot_fieldAccessorTable
            .ensureFieldAccessorsInitialized(ICloud.MBSSnapshot.class, ICloud.MBSSnapshot.Builder.class);
      }

      // Construct using Icloud.MBSSnapshot.newBuilder()
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
          getAttributesFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        snapshotID_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        quotaReserved_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000002);
        lastModified_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000004);
        if (attributesBuilder_ == null) {
          attributes_ = ICloud.MBSSnapshotAttributes.getDefaultInstance();
        } else {
          attributesBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        committed_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ICloud.internal_static_MBSSnapshot_descriptor;
      }

      public ICloud.MBSSnapshot getDefaultInstanceForType() {
        return ICloud.MBSSnapshot.getDefaultInstance();
      }

      public ICloud.MBSSnapshot build() {
        ICloud.MBSSnapshot result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ICloud.MBSSnapshot buildPartial() {
        ICloud.MBSSnapshot result = new ICloud.MBSSnapshot(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.snapshotID_ = snapshotID_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.quotaReserved_ = quotaReserved_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.lastModified_ = lastModified_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        if (attributesBuilder_ == null) {
          result.attributes_ = attributes_;
        } else {
          result.attributes_ = attributesBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.committed_ = committed_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ICloud.MBSSnapshot) {
          return mergeFrom((ICloud.MBSSnapshot)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ICloud.MBSSnapshot other) {
        if (other == ICloud.MBSSnapshot.getDefaultInstance()) return this;
        if (other.hasSnapshotID()) {
          setSnapshotID(other.getSnapshotID());
        }
        if (other.hasQuotaReserved()) {
          setQuotaReserved(other.getQuotaReserved());
        }
        if (other.hasLastModified()) {
          setLastModified(other.getLastModified());
        }
        if (other.hasAttributes()) {
          mergeAttributes(other.getAttributes());
        }
        if (other.hasCommitted()) {
          setCommitted(other.getCommitted());
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
        ICloud.MBSSnapshot parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ICloud.MBSSnapshot) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int snapshotID_ ;
      /**
       * <code>optional uint32 SnapshotID = 1;</code>
       */
      public boolean hasSnapshotID() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional uint32 SnapshotID = 1;</code>
       */
      public int getSnapshotID() {
        return snapshotID_;
      }
      /**
       * <code>optional uint32 SnapshotID = 1;</code>
       */
      public Builder setSnapshotID(int value) {
        bitField0_ |= 0x00000001;
        snapshotID_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 SnapshotID = 1;</code>
       */
      public Builder clearSnapshotID() {
        bitField0_ = (bitField0_ & ~0x00000001);
        snapshotID_ = 0;
        onChanged();
        return this;
      }

      private long quotaReserved_ ;
      /**
       * <code>optional uint64 QuotaReserved = 2;</code>
       */
      public boolean hasQuotaReserved() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional uint64 QuotaReserved = 2;</code>
       */
      public long getQuotaReserved() {
        return quotaReserved_;
      }
      /**
       * <code>optional uint64 QuotaReserved = 2;</code>
       */
      public Builder setQuotaReserved(long value) {
        bitField0_ |= 0x00000002;
        quotaReserved_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint64 QuotaReserved = 2;</code>
       */
      public Builder clearQuotaReserved() {
        bitField0_ = (bitField0_ & ~0x00000002);
        quotaReserved_ = 0L;
        onChanged();
        return this;
      }

      private long lastModified_ ;
      /**
       * <code>optional uint64 LastModified = 3;</code>
       */
      public boolean hasLastModified() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional uint64 LastModified = 3;</code>
       */
      public long getLastModified() {
        return lastModified_;
      }
      /**
       * <code>optional uint64 LastModified = 3;</code>
       */
      public Builder setLastModified(long value) {
        bitField0_ |= 0x00000004;
        lastModified_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint64 LastModified = 3;</code>
       */
      public Builder clearLastModified() {
        bitField0_ = (bitField0_ & ~0x00000004);
        lastModified_ = 0L;
        onChanged();
        return this;
      }

      private ICloud.MBSSnapshotAttributes attributes_ = ICloud.MBSSnapshotAttributes.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          ICloud.MBSSnapshotAttributes, ICloud.MBSSnapshotAttributes.Builder, ICloud.MBSSnapshotAttributesOrBuilder> attributesBuilder_;
      /**
       * <code>optional .MBSSnapshotAttributes Attributes = 5;</code>
       */
      public boolean hasAttributes() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>optional .MBSSnapshotAttributes Attributes = 5;</code>
       */
      public ICloud.MBSSnapshotAttributes getAttributes() {
        if (attributesBuilder_ == null) {
          return attributes_;
        } else {
          return attributesBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .MBSSnapshotAttributes Attributes = 5;</code>
       */
      public Builder setAttributes(ICloud.MBSSnapshotAttributes value) {
        if (attributesBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          attributes_ = value;
          onChanged();
        } else {
          attributesBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .MBSSnapshotAttributes Attributes = 5;</code>
       */
      public Builder setAttributes(
          ICloud.MBSSnapshotAttributes.Builder builderForValue) {
        if (attributesBuilder_ == null) {
          attributes_ = builderForValue.build();
          onChanged();
        } else {
          attributesBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .MBSSnapshotAttributes Attributes = 5;</code>
       */
      public Builder mergeAttributes(ICloud.MBSSnapshotAttributes value) {
        if (attributesBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008) &&
              attributes_ != ICloud.MBSSnapshotAttributes.getDefaultInstance()) {
            attributes_ =
              ICloud.MBSSnapshotAttributes.newBuilder(attributes_).mergeFrom(value).buildPartial();
          } else {
            attributes_ = value;
          }
          onChanged();
        } else {
          attributesBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .MBSSnapshotAttributes Attributes = 5;</code>
       */
      public Builder clearAttributes() {
        if (attributesBuilder_ == null) {
          attributes_ = ICloud.MBSSnapshotAttributes.getDefaultInstance();
          onChanged();
        } else {
          attributesBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      /**
       * <code>optional .MBSSnapshotAttributes Attributes = 5;</code>
       */
      public ICloud.MBSSnapshotAttributes.Builder getAttributesBuilder() {
        bitField0_ |= 0x00000008;
        onChanged();
        return getAttributesFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .MBSSnapshotAttributes Attributes = 5;</code>
       */
      public ICloud.MBSSnapshotAttributesOrBuilder getAttributesOrBuilder() {
        if (attributesBuilder_ != null) {
          return attributesBuilder_.getMessageOrBuilder();
        } else {
          return attributes_;
        }
      }
      /**
       * <code>optional .MBSSnapshotAttributes Attributes = 5;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          ICloud.MBSSnapshotAttributes, ICloud.MBSSnapshotAttributes.Builder, ICloud.MBSSnapshotAttributesOrBuilder> 
          getAttributesFieldBuilder() {
        if (attributesBuilder_ == null) {
          attributesBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              ICloud.MBSSnapshotAttributes, ICloud.MBSSnapshotAttributes.Builder, ICloud.MBSSnapshotAttributesOrBuilder>(
                  getAttributes(),
                  getParentForChildren(),
                  isClean());
          attributes_ = null;
        }
        return attributesBuilder_;
      }

      private long committed_ ;
      /**
       * <code>optional uint64 Committed = 6;</code>
       */
      public boolean hasCommitted() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      /**
       * <code>optional uint64 Committed = 6;</code>
       */
      public long getCommitted() {
        return committed_;
      }
      /**
       * <code>optional uint64 Committed = 6;</code>
       */
      public Builder setCommitted(long value) {
        bitField0_ |= 0x00000010;
        committed_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint64 Committed = 6;</code>
       */
      public Builder clearCommitted() {
        bitField0_ = (bitField0_ & ~0x00000010);
        committed_ = 0L;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:MBSSnapshot)
    }

    static {
      defaultInstance = new MBSSnapshot(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:MBSSnapshot)
  }

  public interface MBSSnapshotAttributesOrBuilder extends
      // @@protoc_insertion_point(interface_extends:MBSSnapshotAttributes)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string DeviceName = 1;</code>
     */
    boolean hasDeviceName();
    /**
     * <code>optional string DeviceName = 1;</code>
     */
    java.lang.String getDeviceName();
    /**
     * <code>optional string DeviceName = 1;</code>
     */
    com.google.protobuf.ByteString
        getDeviceNameBytes();

    /**
     * <code>optional string ProductVersion = 2;</code>
     */
    boolean hasProductVersion();
    /**
     * <code>optional string ProductVersion = 2;</code>
     */
    java.lang.String getProductVersion();
    /**
     * <code>optional string ProductVersion = 2;</code>
     */
    com.google.protobuf.ByteString
        getProductVersionBytes();

    /**
     * <code>optional string BuildVersion = 3;</code>
     */
    boolean hasBuildVersion();
    /**
     * <code>optional string BuildVersion = 3;</code>
     */
    java.lang.String getBuildVersion();
    /**
     * <code>optional string BuildVersion = 3;</code>
     */
    com.google.protobuf.ByteString
        getBuildVersionBytes();

    /**
     * <code>optional uint32 KeybagID = 4;</code>
     */
    boolean hasKeybagID();
    /**
     * <code>optional uint32 KeybagID = 4;</code>
     */
    int getKeybagID();

    /**
     * <code>optional bytes KeybagUUID = 5;</code>
     */
    boolean hasKeybagUUID();
    /**
     * <code>optional bytes KeybagUUID = 5;</code>
     */
    com.google.protobuf.ByteString getKeybagUUID();

    /**
     * <code>optional int32 BackupReason = 6;</code>
     */
    boolean hasBackupReason();
    /**
     * <code>optional int32 BackupReason = 6;</code>
     */
    int getBackupReason();

    /**
     * <code>optional int32 BackupType = 7;</code>
     */
    boolean hasBackupType();
    /**
     * <code>optional int32 BackupType = 7;</code>
     */
    int getBackupType();
  }
  /**
   * Protobuf type {@code MBSSnapshotAttributes}
   */
  public static final class MBSSnapshotAttributes extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:MBSSnapshotAttributes)
      MBSSnapshotAttributesOrBuilder {
    // Use MBSSnapshotAttributes.newBuilder() to construct.
    private MBSSnapshotAttributes(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private MBSSnapshotAttributes(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final MBSSnapshotAttributes defaultInstance;
    public static MBSSnapshotAttributes getDefaultInstance() {
      return defaultInstance;
    }

    public MBSSnapshotAttributes getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private MBSSnapshotAttributes(
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
              deviceName_ = bs;
              break;
            }
            case 18: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000002;
              productVersion_ = bs;
              break;
            }
            case 26: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000004;
              buildVersion_ = bs;
              break;
            }
            case 32: {
              bitField0_ |= 0x00000008;
              keybagID_ = input.readUInt32();
              break;
            }
            case 42: {
              bitField0_ |= 0x00000010;
              keybagUUID_ = input.readBytes();
              break;
            }
            case 48: {
              bitField0_ |= 0x00000020;
              backupReason_ = input.readInt32();
              break;
            }
            case 56: {
              bitField0_ |= 0x00000040;
              backupType_ = input.readInt32();
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
      return ICloud.internal_static_MBSSnapshotAttributes_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ICloud.internal_static_MBSSnapshotAttributes_fieldAccessorTable
          .ensureFieldAccessorsInitialized(ICloud.MBSSnapshotAttributes.class, ICloud.MBSSnapshotAttributes.Builder.class);
    }

    public static com.google.protobuf.Parser<MBSSnapshotAttributes> PARSER =
        new com.google.protobuf.AbstractParser<MBSSnapshotAttributes>() {
      public MBSSnapshotAttributes parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MBSSnapshotAttributes(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<MBSSnapshotAttributes> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int DEVICENAME_FIELD_NUMBER = 1;
    private java.lang.Object deviceName_;
    /**
     * <code>optional string DeviceName = 1;</code>
     */
    public boolean hasDeviceName() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional string DeviceName = 1;</code>
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
     * <code>optional string DeviceName = 1;</code>
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

    public static final int PRODUCTVERSION_FIELD_NUMBER = 2;
    private java.lang.Object productVersion_;
    /**
     * <code>optional string ProductVersion = 2;</code>
     */
    public boolean hasProductVersion() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional string ProductVersion = 2;</code>
     */
    public java.lang.String getProductVersion() {
      java.lang.Object ref = productVersion_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          productVersion_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string ProductVersion = 2;</code>
     */
    public com.google.protobuf.ByteString
        getProductVersionBytes() {
      java.lang.Object ref = productVersion_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        productVersion_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int BUILDVERSION_FIELD_NUMBER = 3;
    private java.lang.Object buildVersion_;
    /**
     * <code>optional string BuildVersion = 3;</code>
     */
    public boolean hasBuildVersion() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional string BuildVersion = 3;</code>
     */
    public java.lang.String getBuildVersion() {
      java.lang.Object ref = buildVersion_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          buildVersion_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string BuildVersion = 3;</code>
     */
    public com.google.protobuf.ByteString
        getBuildVersionBytes() {
      java.lang.Object ref = buildVersion_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        buildVersion_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int KEYBAGID_FIELD_NUMBER = 4;
    private int keybagID_;
    /**
     * <code>optional uint32 KeybagID = 4;</code>
     */
    public boolean hasKeybagID() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional uint32 KeybagID = 4;</code>
     */
    public int getKeybagID() {
      return keybagID_;
    }

    public static final int KEYBAGUUID_FIELD_NUMBER = 5;
    private com.google.protobuf.ByteString keybagUUID_;
    /**
     * <code>optional bytes KeybagUUID = 5;</code>
     */
    public boolean hasKeybagUUID() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    /**
     * <code>optional bytes KeybagUUID = 5;</code>
     */
    public com.google.protobuf.ByteString getKeybagUUID() {
      return keybagUUID_;
    }

    public static final int BACKUPREASON_FIELD_NUMBER = 6;
    private int backupReason_;
    /**
     * <code>optional int32 BackupReason = 6;</code>
     */
    public boolean hasBackupReason() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    /**
     * <code>optional int32 BackupReason = 6;</code>
     */
    public int getBackupReason() {
      return backupReason_;
    }

    public static final int BACKUPTYPE_FIELD_NUMBER = 7;
    private int backupType_;
    /**
     * <code>optional int32 BackupType = 7;</code>
     */
    public boolean hasBackupType() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }
    /**
     * <code>optional int32 BackupType = 7;</code>
     */
    public int getBackupType() {
      return backupType_;
    }

    private void initFields() {
      deviceName_ = "";
      productVersion_ = "";
      buildVersion_ = "";
      keybagID_ = 0;
      keybagUUID_ = com.google.protobuf.ByteString.EMPTY;
      backupReason_ = 0;
      backupType_ = 0;
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
        output.writeBytes(1, getDeviceNameBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getProductVersionBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, getBuildVersionBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeUInt32(4, keybagID_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(5, keybagUUID_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeInt32(6, backupReason_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeInt32(7, backupType_);
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
          .computeBytesSize(1, getDeviceNameBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getProductVersionBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getBuildVersionBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(4, keybagID_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(5, keybagUUID_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(6, backupReason_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(7, backupType_);
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

    public static ICloud.MBSSnapshotAttributes parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSSnapshotAttributes parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSSnapshotAttributes parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSSnapshotAttributes parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSSnapshotAttributes parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSSnapshotAttributes parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static ICloud.MBSSnapshotAttributes parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static ICloud.MBSSnapshotAttributes parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static ICloud.MBSSnapshotAttributes parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSSnapshotAttributes parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(ICloud.MBSSnapshotAttributes prototype) {
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
     * Protobuf type {@code MBSSnapshotAttributes}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:MBSSnapshotAttributes)
        ICloud.MBSSnapshotAttributesOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ICloud.internal_static_MBSSnapshotAttributes_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ICloud.internal_static_MBSSnapshotAttributes_fieldAccessorTable
            .ensureFieldAccessorsInitialized(ICloud.MBSSnapshotAttributes.class, ICloud.MBSSnapshotAttributes.Builder.class);
      }

      // Construct using Icloud.MBSSnapshotAttributes.newBuilder()
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
        deviceName_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        productVersion_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        buildVersion_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        keybagID_ = 0;
        bitField0_ = (bitField0_ & ~0x00000008);
        keybagUUID_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000010);
        backupReason_ = 0;
        bitField0_ = (bitField0_ & ~0x00000020);
        backupType_ = 0;
        bitField0_ = (bitField0_ & ~0x00000040);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ICloud.internal_static_MBSSnapshotAttributes_descriptor;
      }

      public ICloud.MBSSnapshotAttributes getDefaultInstanceForType() {
        return ICloud.MBSSnapshotAttributes.getDefaultInstance();
      }

      public ICloud.MBSSnapshotAttributes build() {
        ICloud.MBSSnapshotAttributes result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ICloud.MBSSnapshotAttributes buildPartial() {
        ICloud.MBSSnapshotAttributes result = new ICloud.MBSSnapshotAttributes(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.deviceName_ = deviceName_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.productVersion_ = productVersion_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.buildVersion_ = buildVersion_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.keybagID_ = keybagID_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.keybagUUID_ = keybagUUID_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.backupReason_ = backupReason_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000040;
        }
        result.backupType_ = backupType_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ICloud.MBSSnapshotAttributes) {
          return mergeFrom((ICloud.MBSSnapshotAttributes)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ICloud.MBSSnapshotAttributes other) {
        if (other == ICloud.MBSSnapshotAttributes.getDefaultInstance()) return this;
        if (other.hasDeviceName()) {
          bitField0_ |= 0x00000001;
          deviceName_ = other.deviceName_;
          onChanged();
        }
        if (other.hasProductVersion()) {
          bitField0_ |= 0x00000002;
          productVersion_ = other.productVersion_;
          onChanged();
        }
        if (other.hasBuildVersion()) {
          bitField0_ |= 0x00000004;
          buildVersion_ = other.buildVersion_;
          onChanged();
        }
        if (other.hasKeybagID()) {
          setKeybagID(other.getKeybagID());
        }
        if (other.hasKeybagUUID()) {
          setKeybagUUID(other.getKeybagUUID());
        }
        if (other.hasBackupReason()) {
          setBackupReason(other.getBackupReason());
        }
        if (other.hasBackupType()) {
          setBackupType(other.getBackupType());
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
        ICloud.MBSSnapshotAttributes parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ICloud.MBSSnapshotAttributes) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object deviceName_ = "";
      /**
       * <code>optional string DeviceName = 1;</code>
       */
      public boolean hasDeviceName() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional string DeviceName = 1;</code>
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
       * <code>optional string DeviceName = 1;</code>
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
       * <code>optional string DeviceName = 1;</code>
       */
      public Builder setDeviceName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        deviceName_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string DeviceName = 1;</code>
       */
      public Builder clearDeviceName() {
        bitField0_ = (bitField0_ & ~0x00000001);
        deviceName_ = getDefaultInstance().getDeviceName();
        onChanged();
        return this;
      }
      /**
       * <code>optional string DeviceName = 1;</code>
       */
      public Builder setDeviceNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        deviceName_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object productVersion_ = "";
      /**
       * <code>optional string ProductVersion = 2;</code>
       */
      public boolean hasProductVersion() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional string ProductVersion = 2;</code>
       */
      public java.lang.String getProductVersion() {
        java.lang.Object ref = productVersion_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            productVersion_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string ProductVersion = 2;</code>
       */
      public com.google.protobuf.ByteString
          getProductVersionBytes() {
        java.lang.Object ref = productVersion_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          productVersion_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string ProductVersion = 2;</code>
       */
      public Builder setProductVersion(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        productVersion_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string ProductVersion = 2;</code>
       */
      public Builder clearProductVersion() {
        bitField0_ = (bitField0_ & ~0x00000002);
        productVersion_ = getDefaultInstance().getProductVersion();
        onChanged();
        return this;
      }
      /**
       * <code>optional string ProductVersion = 2;</code>
       */
      public Builder setProductVersionBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        productVersion_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object buildVersion_ = "";
      /**
       * <code>optional string BuildVersion = 3;</code>
       */
      public boolean hasBuildVersion() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional string BuildVersion = 3;</code>
       */
      public java.lang.String getBuildVersion() {
        java.lang.Object ref = buildVersion_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            buildVersion_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string BuildVersion = 3;</code>
       */
      public com.google.protobuf.ByteString
          getBuildVersionBytes() {
        java.lang.Object ref = buildVersion_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          buildVersion_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string BuildVersion = 3;</code>
       */
      public Builder setBuildVersion(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        buildVersion_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string BuildVersion = 3;</code>
       */
      public Builder clearBuildVersion() {
        bitField0_ = (bitField0_ & ~0x00000004);
        buildVersion_ = getDefaultInstance().getBuildVersion();
        onChanged();
        return this;
      }
      /**
       * <code>optional string BuildVersion = 3;</code>
       */
      public Builder setBuildVersionBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        buildVersion_ = value;
        onChanged();
        return this;
      }

      private int keybagID_ ;
      /**
       * <code>optional uint32 KeybagID = 4;</code>
       */
      public boolean hasKeybagID() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>optional uint32 KeybagID = 4;</code>
       */
      public int getKeybagID() {
        return keybagID_;
      }
      /**
       * <code>optional uint32 KeybagID = 4;</code>
       */
      public Builder setKeybagID(int value) {
        bitField0_ |= 0x00000008;
        keybagID_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 KeybagID = 4;</code>
       */
      public Builder clearKeybagID() {
        bitField0_ = (bitField0_ & ~0x00000008);
        keybagID_ = 0;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString keybagUUID_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes KeybagUUID = 5;</code>
       */
      public boolean hasKeybagUUID() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      /**
       * <code>optional bytes KeybagUUID = 5;</code>
       */
      public com.google.protobuf.ByteString getKeybagUUID() {
        return keybagUUID_;
      }
      /**
       * <code>optional bytes KeybagUUID = 5;</code>
       */
      public Builder setKeybagUUID(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        keybagUUID_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes KeybagUUID = 5;</code>
       */
      public Builder clearKeybagUUID() {
        bitField0_ = (bitField0_ & ~0x00000010);
        keybagUUID_ = getDefaultInstance().getKeybagUUID();
        onChanged();
        return this;
      }

      private int backupReason_ ;
      /**
       * <code>optional int32 BackupReason = 6;</code>
       */
      public boolean hasBackupReason() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      /**
       * <code>optional int32 BackupReason = 6;</code>
       */
      public int getBackupReason() {
        return backupReason_;
      }
      /**
       * <code>optional int32 BackupReason = 6;</code>
       */
      public Builder setBackupReason(int value) {
        bitField0_ |= 0x00000020;
        backupReason_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 BackupReason = 6;</code>
       */
      public Builder clearBackupReason() {
        bitField0_ = (bitField0_ & ~0x00000020);
        backupReason_ = 0;
        onChanged();
        return this;
      }

      private int backupType_ ;
      /**
       * <code>optional int32 BackupType = 7;</code>
       */
      public boolean hasBackupType() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }
      /**
       * <code>optional int32 BackupType = 7;</code>
       */
      public int getBackupType() {
        return backupType_;
      }
      /**
       * <code>optional int32 BackupType = 7;</code>
       */
      public Builder setBackupType(int value) {
        bitField0_ |= 0x00000040;
        backupType_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 BackupType = 7;</code>
       */
      public Builder clearBackupType() {
        bitField0_ = (bitField0_ & ~0x00000040);
        backupType_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:MBSSnapshotAttributes)
    }

    static {
      defaultInstance = new MBSSnapshotAttributes(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:MBSSnapshotAttributes)
  }

  public interface MBSSnapshotIDOrBuilder extends
      // @@protoc_insertion_point(interface_extends:MBSSnapshotID)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional uint32 SnapshotID = 1;</code>
     */
    boolean hasSnapshotID();
    /**
     * <code>optional uint32 SnapshotID = 1;</code>
     */
    int getSnapshotID();
  }
  /**
   * Protobuf type {@code MBSSnapshotID}
   */
  public static final class MBSSnapshotID extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:MBSSnapshotID)
      MBSSnapshotIDOrBuilder {
    // Use MBSSnapshotID.newBuilder() to construct.
    private MBSSnapshotID(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private MBSSnapshotID(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final MBSSnapshotID defaultInstance;
    public static MBSSnapshotID getDefaultInstance() {
      return defaultInstance;
    }

    public MBSSnapshotID getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private MBSSnapshotID(
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
              snapshotID_ = input.readUInt32();
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
      return ICloud.internal_static_MBSSnapshotID_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ICloud.internal_static_MBSSnapshotID_fieldAccessorTable
          .ensureFieldAccessorsInitialized(ICloud.MBSSnapshotID.class, ICloud.MBSSnapshotID.Builder.class);
    }

    public static com.google.protobuf.Parser<MBSSnapshotID> PARSER =
        new com.google.protobuf.AbstractParser<MBSSnapshotID>() {
      public MBSSnapshotID parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MBSSnapshotID(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<MBSSnapshotID> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int SNAPSHOTID_FIELD_NUMBER = 1;
    private int snapshotID_;
    /**
     * <code>optional uint32 SnapshotID = 1;</code>
     */
    public boolean hasSnapshotID() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional uint32 SnapshotID = 1;</code>
     */
    public int getSnapshotID() {
      return snapshotID_;
    }

    private void initFields() {
      snapshotID_ = 0;
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
        output.writeUInt32(1, snapshotID_);
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
          .computeUInt32Size(1, snapshotID_);
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

    public static ICloud.MBSSnapshotID parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSSnapshotID parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSSnapshotID parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSSnapshotID parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSSnapshotID parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSSnapshotID parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static ICloud.MBSSnapshotID parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static ICloud.MBSSnapshotID parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static ICloud.MBSSnapshotID parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSSnapshotID parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(ICloud.MBSSnapshotID prototype) {
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
     * Protobuf type {@code MBSSnapshotID}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:MBSSnapshotID)
        ICloud.MBSSnapshotIDOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ICloud.internal_static_MBSSnapshotID_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ICloud.internal_static_MBSSnapshotID_fieldAccessorTable
            .ensureFieldAccessorsInitialized(ICloud.MBSSnapshotID.class, ICloud.MBSSnapshotID.Builder.class);
      }

      // Construct using Icloud.MBSSnapshotID.newBuilder()
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
        snapshotID_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ICloud.internal_static_MBSSnapshotID_descriptor;
      }

      public ICloud.MBSSnapshotID getDefaultInstanceForType() {
        return ICloud.MBSSnapshotID.getDefaultInstance();
      }

      public ICloud.MBSSnapshotID build() {
        ICloud.MBSSnapshotID result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ICloud.MBSSnapshotID buildPartial() {
        ICloud.MBSSnapshotID result = new ICloud.MBSSnapshotID(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.snapshotID_ = snapshotID_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ICloud.MBSSnapshotID) {
          return mergeFrom((ICloud.MBSSnapshotID)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ICloud.MBSSnapshotID other) {
        if (other == ICloud.MBSSnapshotID.getDefaultInstance()) return this;
        if (other.hasSnapshotID()) {
          setSnapshotID(other.getSnapshotID());
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
        ICloud.MBSSnapshotID parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ICloud.MBSSnapshotID) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int snapshotID_ ;
      /**
       * <code>optional uint32 SnapshotID = 1;</code>
       */
      public boolean hasSnapshotID() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional uint32 SnapshotID = 1;</code>
       */
      public int getSnapshotID() {
        return snapshotID_;
      }
      /**
       * <code>optional uint32 SnapshotID = 1;</code>
       */
      public Builder setSnapshotID(int value) {
        bitField0_ |= 0x00000001;
        snapshotID_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 SnapshotID = 1;</code>
       */
      public Builder clearSnapshotID() {
        bitField0_ = (bitField0_ & ~0x00000001);
        snapshotID_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:MBSSnapshotID)
    }

    static {
      defaultInstance = new MBSSnapshotID(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:MBSSnapshotID)
  }

  public interface MBSFileAuthTokenOrBuilder extends
      // @@protoc_insertion_point(interface_extends:MBSFileAuthToken)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>required bytes FileID = 1;</code>
     */
    boolean hasFileID();
    /**
     * <code>required bytes FileID = 1;</code>
     */
    com.google.protobuf.ByteString getFileID();

    /**
     * <code>optional string AuthToken = 2;</code>
     */
    boolean hasAuthToken();
    /**
     * <code>optional string AuthToken = 2;</code>
     */
    java.lang.String getAuthToken();
    /**
     * <code>optional string AuthToken = 2;</code>
     */
    com.google.protobuf.ByteString
        getAuthTokenBytes();
  }
  /**
   * Protobuf type {@code MBSFileAuthToken}
   */
  public static final class MBSFileAuthToken extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:MBSFileAuthToken)
      MBSFileAuthTokenOrBuilder {
    // Use MBSFileAuthToken.newBuilder() to construct.
    private MBSFileAuthToken(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private MBSFileAuthToken(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final MBSFileAuthToken defaultInstance;
    public static MBSFileAuthToken getDefaultInstance() {
      return defaultInstance;
    }

    public MBSFileAuthToken getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private MBSFileAuthToken(
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
              fileID_ = input.readBytes();
              break;
            }
            case 18: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000002;
              authToken_ = bs;
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
      return ICloud.internal_static_MBSFileAuthToken_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ICloud.internal_static_MBSFileAuthToken_fieldAccessorTable
          .ensureFieldAccessorsInitialized(ICloud.MBSFileAuthToken.class, ICloud.MBSFileAuthToken.Builder.class);
    }

    public static com.google.protobuf.Parser<MBSFileAuthToken> PARSER =
        new com.google.protobuf.AbstractParser<MBSFileAuthToken>() {
      public MBSFileAuthToken parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MBSFileAuthToken(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<MBSFileAuthToken> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int FILEID_FIELD_NUMBER = 1;
    private com.google.protobuf.ByteString fileID_;
    /**
     * <code>required bytes FileID = 1;</code>
     */
    public boolean hasFileID() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required bytes FileID = 1;</code>
     */
    public com.google.protobuf.ByteString getFileID() {
      return fileID_;
    }

    public static final int AUTHTOKEN_FIELD_NUMBER = 2;
    private java.lang.Object authToken_;
    /**
     * <code>optional string AuthToken = 2;</code>
     */
    public boolean hasAuthToken() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional string AuthToken = 2;</code>
     */
    public java.lang.String getAuthToken() {
      java.lang.Object ref = authToken_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          authToken_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string AuthToken = 2;</code>
     */
    public com.google.protobuf.ByteString
        getAuthTokenBytes() {
      java.lang.Object ref = authToken_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        authToken_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private void initFields() {
      fileID_ = com.google.protobuf.ByteString.EMPTY;
      authToken_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasFileID()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, fileID_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getAuthTokenBytes());
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
          .computeBytesSize(1, fileID_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getAuthTokenBytes());
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

    public static ICloud.MBSFileAuthToken parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSFileAuthToken parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSFileAuthToken parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSFileAuthToken parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSFileAuthToken parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSFileAuthToken parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static ICloud.MBSFileAuthToken parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static ICloud.MBSFileAuthToken parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static ICloud.MBSFileAuthToken parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSFileAuthToken parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(ICloud.MBSFileAuthToken prototype) {
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
     * Protobuf type {@code MBSFileAuthToken}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:MBSFileAuthToken)
        ICloud.MBSFileAuthTokenOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ICloud.internal_static_MBSFileAuthToken_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ICloud.internal_static_MBSFileAuthToken_fieldAccessorTable
            .ensureFieldAccessorsInitialized(ICloud.MBSFileAuthToken.class, ICloud.MBSFileAuthToken.Builder.class);
      }

      // Construct using Icloud.MBSFileAuthToken.newBuilder()
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
        fileID_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        authToken_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ICloud.internal_static_MBSFileAuthToken_descriptor;
      }

      public ICloud.MBSFileAuthToken getDefaultInstanceForType() {
        return ICloud.MBSFileAuthToken.getDefaultInstance();
      }

      public ICloud.MBSFileAuthToken build() {
        ICloud.MBSFileAuthToken result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ICloud.MBSFileAuthToken buildPartial() {
        ICloud.MBSFileAuthToken result = new ICloud.MBSFileAuthToken(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.fileID_ = fileID_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.authToken_ = authToken_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ICloud.MBSFileAuthToken) {
          return mergeFrom((ICloud.MBSFileAuthToken)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ICloud.MBSFileAuthToken other) {
        if (other == ICloud.MBSFileAuthToken.getDefaultInstance()) return this;
        if (other.hasFileID()) {
          setFileID(other.getFileID());
        }
        if (other.hasAuthToken()) {
          bitField0_ |= 0x00000002;
          authToken_ = other.authToken_;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasFileID()) {
          
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        ICloud.MBSFileAuthToken parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ICloud.MBSFileAuthToken) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.google.protobuf.ByteString fileID_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>required bytes FileID = 1;</code>
       */
      public boolean hasFileID() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required bytes FileID = 1;</code>
       */
      public com.google.protobuf.ByteString getFileID() {
        return fileID_;
      }
      /**
       * <code>required bytes FileID = 1;</code>
       */
      public Builder setFileID(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        fileID_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required bytes FileID = 1;</code>
       */
      public Builder clearFileID() {
        bitField0_ = (bitField0_ & ~0x00000001);
        fileID_ = getDefaultInstance().getFileID();
        onChanged();
        return this;
      }

      private java.lang.Object authToken_ = "";
      /**
       * <code>optional string AuthToken = 2;</code>
       */
      public boolean hasAuthToken() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional string AuthToken = 2;</code>
       */
      public java.lang.String getAuthToken() {
        java.lang.Object ref = authToken_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            authToken_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string AuthToken = 2;</code>
       */
      public com.google.protobuf.ByteString
          getAuthTokenBytes() {
        java.lang.Object ref = authToken_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          authToken_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string AuthToken = 2;</code>
       */
      public Builder setAuthToken(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        authToken_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string AuthToken = 2;</code>
       */
      public Builder clearAuthToken() {
        bitField0_ = (bitField0_ & ~0x00000002);
        authToken_ = getDefaultInstance().getAuthToken();
        onChanged();
        return this;
      }
      /**
       * <code>optional string AuthToken = 2;</code>
       */
      public Builder setAuthTokenBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        authToken_ = value;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:MBSFileAuthToken)
    }

    static {
      defaultInstance = new MBSFileAuthToken(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:MBSFileAuthToken)
  }

  public interface MBSFileAuthTokensOrBuilder extends
      // @@protoc_insertion_point(interface_extends:MBSFileAuthTokens)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>repeated .MBSFileAuthToken tokens = 1;</code>
     */
    java.util.List<ICloud.MBSFileAuthToken> 
        getTokensList();
    /**
     * <code>repeated .MBSFileAuthToken tokens = 1;</code>
     */
    ICloud.MBSFileAuthToken getTokens(int index);
    /**
     * <code>repeated .MBSFileAuthToken tokens = 1;</code>
     */
    int getTokensCount();
    /**
     * <code>repeated .MBSFileAuthToken tokens = 1;</code>
     */
    java.util.List<? extends ICloud.MBSFileAuthTokenOrBuilder> 
        getTokensOrBuilderList();
    /**
     * <code>repeated .MBSFileAuthToken tokens = 1;</code>
     */
    ICloud.MBSFileAuthTokenOrBuilder getTokensOrBuilder(
        int index);
  }
  /**
   * Protobuf type {@code MBSFileAuthTokens}
   */
  public static final class MBSFileAuthTokens extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:MBSFileAuthTokens)
      MBSFileAuthTokensOrBuilder {
    // Use MBSFileAuthTokens.newBuilder() to construct.
    private MBSFileAuthTokens(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private MBSFileAuthTokens(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final MBSFileAuthTokens defaultInstance;
    public static MBSFileAuthTokens getDefaultInstance() {
      return defaultInstance;
    }

    public MBSFileAuthTokens getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private MBSFileAuthTokens(
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
              if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
                tokens_ = new java.util.ArrayList<ICloud.MBSFileAuthToken>();
                mutable_bitField0_ |= 0x00000001;
              }
              tokens_.add(input.readMessage(ICloud.MBSFileAuthToken.PARSER, extensionRegistry));
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
        if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
          tokens_ = java.util.Collections.unmodifiableList(tokens_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return ICloud.internal_static_MBSFileAuthTokens_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ICloud.internal_static_MBSFileAuthTokens_fieldAccessorTable
          .ensureFieldAccessorsInitialized(ICloud.MBSFileAuthTokens.class, ICloud.MBSFileAuthTokens.Builder.class);
    }

    public static com.google.protobuf.Parser<MBSFileAuthTokens> PARSER =
        new com.google.protobuf.AbstractParser<MBSFileAuthTokens>() {
      public MBSFileAuthTokens parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MBSFileAuthTokens(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<MBSFileAuthTokens> getParserForType() {
      return PARSER;
    }

    public static final int TOKENS_FIELD_NUMBER = 1;
    private java.util.List<ICloud.MBSFileAuthToken> tokens_;
    /**
     * <code>repeated .MBSFileAuthToken tokens = 1;</code>
     */
    public java.util.List<ICloud.MBSFileAuthToken> getTokensList() {
      return tokens_;
    }
    /**
     * <code>repeated .MBSFileAuthToken tokens = 1;</code>
     */
    public java.util.List<? extends ICloud.MBSFileAuthTokenOrBuilder> 
        getTokensOrBuilderList() {
      return tokens_;
    }
    /**
     * <code>repeated .MBSFileAuthToken tokens = 1;</code>
     */
    public int getTokensCount() {
      return tokens_.size();
    }
    /**
     * <code>repeated .MBSFileAuthToken tokens = 1;</code>
     */
    public ICloud.MBSFileAuthToken getTokens(int index) {
      return tokens_.get(index);
    }
    /**
     * <code>repeated .MBSFileAuthToken tokens = 1;</code>
     */
    public ICloud.MBSFileAuthTokenOrBuilder getTokensOrBuilder(
        int index) {
      return tokens_.get(index);
    }

    private void initFields() {
      tokens_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      for (int i = 0; i < getTokensCount(); i++) {
        if (!getTokens(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      for (int i = 0; i < tokens_.size(); i++) {
        output.writeMessage(1, tokens_.get(i));
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      for (int i = 0; i < tokens_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, tokens_.get(i));
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

    public static ICloud.MBSFileAuthTokens parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSFileAuthTokens parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSFileAuthTokens parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSFileAuthTokens parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSFileAuthTokens parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSFileAuthTokens parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static ICloud.MBSFileAuthTokens parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static ICloud.MBSFileAuthTokens parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static ICloud.MBSFileAuthTokens parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSFileAuthTokens parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(ICloud.MBSFileAuthTokens prototype) {
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
     * Protobuf type {@code MBSFileAuthTokens}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:MBSFileAuthTokens)
        ICloud.MBSFileAuthTokensOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ICloud.internal_static_MBSFileAuthTokens_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ICloud.internal_static_MBSFileAuthTokens_fieldAccessorTable
            .ensureFieldAccessorsInitialized(ICloud.MBSFileAuthTokens.class, ICloud.MBSFileAuthTokens.Builder.class);
      }

      // Construct using Icloud.MBSFileAuthTokens.newBuilder()
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
          getTokensFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (tokensBuilder_ == null) {
          tokens_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          tokensBuilder_.clear();
        }
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ICloud.internal_static_MBSFileAuthTokens_descriptor;
      }

      public ICloud.MBSFileAuthTokens getDefaultInstanceForType() {
        return ICloud.MBSFileAuthTokens.getDefaultInstance();
      }

      public ICloud.MBSFileAuthTokens build() {
        ICloud.MBSFileAuthTokens result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ICloud.MBSFileAuthTokens buildPartial() {
        ICloud.MBSFileAuthTokens result = new ICloud.MBSFileAuthTokens(this);
        int from_bitField0_ = bitField0_;
        if (tokensBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001)) {
            tokens_ = java.util.Collections.unmodifiableList(tokens_);
            bitField0_ = (bitField0_ & ~0x00000001);
          }
          result.tokens_ = tokens_;
        } else {
          result.tokens_ = tokensBuilder_.build();
        }
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ICloud.MBSFileAuthTokens) {
          return mergeFrom((ICloud.MBSFileAuthTokens)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ICloud.MBSFileAuthTokens other) {
        if (other == ICloud.MBSFileAuthTokens.getDefaultInstance()) return this;
        if (tokensBuilder_ == null) {
          if (!other.tokens_.isEmpty()) {
            if (tokens_.isEmpty()) {
              tokens_ = other.tokens_;
              bitField0_ = (bitField0_ & ~0x00000001);
            } else {
              ensureTokensIsMutable();
              tokens_.addAll(other.tokens_);
            }
            onChanged();
          }
        } else {
          if (!other.tokens_.isEmpty()) {
            if (tokensBuilder_.isEmpty()) {
              tokensBuilder_.dispose();
              tokensBuilder_ = null;
              tokens_ = other.tokens_;
              bitField0_ = (bitField0_ & ~0x00000001);
              tokensBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getTokensFieldBuilder() : null;
            } else {
              tokensBuilder_.addAllMessages(other.tokens_);
            }
          }
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        for (int i = 0; i < getTokensCount(); i++) {
          if (!getTokens(i).isInitialized()) {
            
            return false;
          }
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        ICloud.MBSFileAuthTokens parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ICloud.MBSFileAuthTokens) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.util.List<ICloud.MBSFileAuthToken> tokens_ =
        java.util.Collections.emptyList();
      private void ensureTokensIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          tokens_ = new java.util.ArrayList<ICloud.MBSFileAuthToken>(tokens_);
          bitField0_ |= 0x00000001;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          ICloud.MBSFileAuthToken, ICloud.MBSFileAuthToken.Builder, ICloud.MBSFileAuthTokenOrBuilder> tokensBuilder_;

      /**
       * <code>repeated .MBSFileAuthToken tokens = 1;</code>
       */
      public java.util.List<ICloud.MBSFileAuthToken> getTokensList() {
        if (tokensBuilder_ == null) {
          return java.util.Collections.unmodifiableList(tokens_);
        } else {
          return tokensBuilder_.getMessageList();
        }
      }
      /**
       * <code>repeated .MBSFileAuthToken tokens = 1;</code>
       */
      public int getTokensCount() {
        if (tokensBuilder_ == null) {
          return tokens_.size();
        } else {
          return tokensBuilder_.getCount();
        }
      }
      /**
       * <code>repeated .MBSFileAuthToken tokens = 1;</code>
       */
      public ICloud.MBSFileAuthToken getTokens(int index) {
        if (tokensBuilder_ == null) {
          return tokens_.get(index);
        } else {
          return tokensBuilder_.getMessage(index);
        }
      }
      /**
       * <code>repeated .MBSFileAuthToken tokens = 1;</code>
       */
      public Builder setTokens(
          int index, ICloud.MBSFileAuthToken value) {
        if (tokensBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureTokensIsMutable();
          tokens_.set(index, value);
          onChanged();
        } else {
          tokensBuilder_.setMessage(index, value);
        }
        return this;
      }
      /**
       * <code>repeated .MBSFileAuthToken tokens = 1;</code>
       */
      public Builder setTokens(
          int index, ICloud.MBSFileAuthToken.Builder builderForValue) {
        if (tokensBuilder_ == null) {
          ensureTokensIsMutable();
          tokens_.set(index, builderForValue.build());
          onChanged();
        } else {
          tokensBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .MBSFileAuthToken tokens = 1;</code>
       */
      public Builder addTokens(ICloud.MBSFileAuthToken value) {
        if (tokensBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureTokensIsMutable();
          tokens_.add(value);
          onChanged();
        } else {
          tokensBuilder_.addMessage(value);
        }
        return this;
      }
      /**
       * <code>repeated .MBSFileAuthToken tokens = 1;</code>
       */
      public Builder addTokens(
          int index, ICloud.MBSFileAuthToken value) {
        if (tokensBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureTokensIsMutable();
          tokens_.add(index, value);
          onChanged();
        } else {
          tokensBuilder_.addMessage(index, value);
        }
        return this;
      }
      /**
       * <code>repeated .MBSFileAuthToken tokens = 1;</code>
       */
      public Builder addTokens(
          ICloud.MBSFileAuthToken.Builder builderForValue) {
        if (tokensBuilder_ == null) {
          ensureTokensIsMutable();
          tokens_.add(builderForValue.build());
          onChanged();
        } else {
          tokensBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .MBSFileAuthToken tokens = 1;</code>
       */
      public Builder addTokens(
          int index, ICloud.MBSFileAuthToken.Builder builderForValue) {
        if (tokensBuilder_ == null) {
          ensureTokensIsMutable();
          tokens_.add(index, builderForValue.build());
          onChanged();
        } else {
          tokensBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .MBSFileAuthToken tokens = 1;</code>
       */
      public Builder addAllTokens(
          java.lang.Iterable<? extends ICloud.MBSFileAuthToken> values) {
        if (tokensBuilder_ == null) {
          ensureTokensIsMutable();
          com.google.protobuf.AbstractMessageLite.Builder.addAll(
              values, tokens_);
          onChanged();
        } else {
          tokensBuilder_.addAllMessages(values);
        }
        return this;
      }
      /**
       * <code>repeated .MBSFileAuthToken tokens = 1;</code>
       */
      public Builder clearTokens() {
        if (tokensBuilder_ == null) {
          tokens_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
          onChanged();
        } else {
          tokensBuilder_.clear();
        }
        return this;
      }
      /**
       * <code>repeated .MBSFileAuthToken tokens = 1;</code>
       */
      public Builder removeTokens(int index) {
        if (tokensBuilder_ == null) {
          ensureTokensIsMutable();
          tokens_.remove(index);
          onChanged();
        } else {
          tokensBuilder_.remove(index);
        }
        return this;
      }
      /**
       * <code>repeated .MBSFileAuthToken tokens = 1;</code>
       */
      public ICloud.MBSFileAuthToken.Builder getTokensBuilder(
          int index) {
        return getTokensFieldBuilder().getBuilder(index);
      }
      /**
       * <code>repeated .MBSFileAuthToken tokens = 1;</code>
       */
      public ICloud.MBSFileAuthTokenOrBuilder getTokensOrBuilder(
          int index) {
        if (tokensBuilder_ == null) {
          return tokens_.get(index);  } else {
          return tokensBuilder_.getMessageOrBuilder(index);
        }
      }
      /**
       * <code>repeated .MBSFileAuthToken tokens = 1;</code>
       */
      public java.util.List<? extends ICloud.MBSFileAuthTokenOrBuilder> 
           getTokensOrBuilderList() {
        if (tokensBuilder_ != null) {
          return tokensBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(tokens_);
        }
      }
      /**
       * <code>repeated .MBSFileAuthToken tokens = 1;</code>
       */
      public ICloud.MBSFileAuthToken.Builder addTokensBuilder() {
        return getTokensFieldBuilder().addBuilder(ICloud.MBSFileAuthToken.getDefaultInstance());
      }
      /**
       * <code>repeated .MBSFileAuthToken tokens = 1;</code>
       */
      public ICloud.MBSFileAuthToken.Builder addTokensBuilder(
          int index) {
        return getTokensFieldBuilder().addBuilder(index, ICloud.MBSFileAuthToken.getDefaultInstance());
      }
      /**
       * <code>repeated .MBSFileAuthToken tokens = 1;</code>
       */
      public java.util.List<ICloud.MBSFileAuthToken.Builder> 
           getTokensBuilderList() {
        return getTokensFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          ICloud.MBSFileAuthToken, ICloud.MBSFileAuthToken.Builder, ICloud.MBSFileAuthTokenOrBuilder> 
          getTokensFieldBuilder() {
        if (tokensBuilder_ == null) {
          tokensBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              ICloud.MBSFileAuthToken, ICloud.MBSFileAuthToken.Builder, ICloud.MBSFileAuthTokenOrBuilder>(
                  tokens_,
                  ((bitField0_ & 0x00000001) == 0x00000001),
                  getParentForChildren(),
                  isClean());
          tokens_ = null;
        }
        return tokensBuilder_;
      }

      // @@protoc_insertion_point(builder_scope:MBSFileAuthTokens)
    }

    static {
      defaultInstance = new MBSFileAuthTokens(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:MBSFileAuthTokens)
  }

  public interface MBSFileIDOrBuilder extends
      // @@protoc_insertion_point(interface_extends:MBSFileID)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>required bytes FileID = 1;</code>
     */
    boolean hasFileID();
    /**
     * <code>required bytes FileID = 1;</code>
     */
    com.google.protobuf.ByteString getFileID();
  }
  /**
   * Protobuf type {@code MBSFileID}
   */
  public static final class MBSFileID extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:MBSFileID)
      MBSFileIDOrBuilder {
    // Use MBSFileID.newBuilder() to construct.
    private MBSFileID(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private MBSFileID(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final MBSFileID defaultInstance;
    public static MBSFileID getDefaultInstance() {
      return defaultInstance;
    }

    public MBSFileID getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private MBSFileID(
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
              fileID_ = input.readBytes();
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
      return ICloud.internal_static_MBSFileID_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ICloud.internal_static_MBSFileID_fieldAccessorTable
          .ensureFieldAccessorsInitialized(ICloud.MBSFileID.class, ICloud.MBSFileID.Builder.class);
    }

    public static com.google.protobuf.Parser<MBSFileID> PARSER =
        new com.google.protobuf.AbstractParser<MBSFileID>() {
      public MBSFileID parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MBSFileID(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<MBSFileID> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int FILEID_FIELD_NUMBER = 1;
    private com.google.protobuf.ByteString fileID_;
    /**
     * <code>required bytes FileID = 1;</code>
     */
    public boolean hasFileID() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required bytes FileID = 1;</code>
     */
    public com.google.protobuf.ByteString getFileID() {
      return fileID_;
    }

    private void initFields() {
      fileID_ = com.google.protobuf.ByteString.EMPTY;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasFileID()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, fileID_);
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
          .computeBytesSize(1, fileID_);
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

    public static ICloud.MBSFileID parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSFileID parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSFileID parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSFileID parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSFileID parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSFileID parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static ICloud.MBSFileID parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static ICloud.MBSFileID parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static ICloud.MBSFileID parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSFileID parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(ICloud.MBSFileID prototype) {
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
     * Protobuf type {@code MBSFileID}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:MBSFileID)
        ICloud.MBSFileIDOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ICloud.internal_static_MBSFileID_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ICloud.internal_static_MBSFileID_fieldAccessorTable
            .ensureFieldAccessorsInitialized(ICloud.MBSFileID.class, ICloud.MBSFileID.Builder.class);
      }

      // Construct using Icloud.MBSFileID.newBuilder()
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
        fileID_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ICloud.internal_static_MBSFileID_descriptor;
      }

      public ICloud.MBSFileID getDefaultInstanceForType() {
        return ICloud.MBSFileID.getDefaultInstance();
      }

      public ICloud.MBSFileID build() {
        ICloud.MBSFileID result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ICloud.MBSFileID buildPartial() {
        ICloud.MBSFileID result = new ICloud.MBSFileID(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.fileID_ = fileID_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ICloud.MBSFileID) {
          return mergeFrom((ICloud.MBSFileID)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ICloud.MBSFileID other) {
        if (other == ICloud.MBSFileID.getDefaultInstance()) return this;
        if (other.hasFileID()) {
          setFileID(other.getFileID());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasFileID()) {
          
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        ICloud.MBSFileID parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ICloud.MBSFileID) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.google.protobuf.ByteString fileID_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>required bytes FileID = 1;</code>
       */
      public boolean hasFileID() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required bytes FileID = 1;</code>
       */
      public com.google.protobuf.ByteString getFileID() {
        return fileID_;
      }
      /**
       * <code>required bytes FileID = 1;</code>
       */
      public Builder setFileID(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        fileID_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required bytes FileID = 1;</code>
       */
      public Builder clearFileID() {
        bitField0_ = (bitField0_ & ~0x00000001);
        fileID_ = getDefaultInstance().getFileID();
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:MBSFileID)
    }

    static {
      defaultInstance = new MBSFileID(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:MBSFileID)
  }

  public interface MBSKeyOrBuilder extends
      // @@protoc_insertion_point(interface_extends:MBSKey)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional uint32 KeyID = 1;</code>
     */
    boolean hasKeyID();
    /**
     * <code>optional uint32 KeyID = 1;</code>
     */
    int getKeyID();

    /**
     * <code>optional bytes KeyData = 2;</code>
     */
    boolean hasKeyData();
    /**
     * <code>optional bytes KeyData = 2;</code>
     */
    com.google.protobuf.ByteString getKeyData();
  }
  /**
   * Protobuf type {@code MBSKey}
   */
  public static final class MBSKey extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:MBSKey)
      MBSKeyOrBuilder {
    // Use MBSKey.newBuilder() to construct.
    private MBSKey(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private MBSKey(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final MBSKey defaultInstance;
    public static MBSKey getDefaultInstance() {
      return defaultInstance;
    }

    public MBSKey getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private MBSKey(
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
              keyID_ = input.readUInt32();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              keyData_ = input.readBytes();
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
      return ICloud.internal_static_MBSKey_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ICloud.internal_static_MBSKey_fieldAccessorTable
          .ensureFieldAccessorsInitialized(ICloud.MBSKey.class, ICloud.MBSKey.Builder.class);
    }

    public static com.google.protobuf.Parser<MBSKey> PARSER =
        new com.google.protobuf.AbstractParser<MBSKey>() {
      public MBSKey parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MBSKey(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<MBSKey> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int KEYID_FIELD_NUMBER = 1;
    private int keyID_;
    /**
     * <code>optional uint32 KeyID = 1;</code>
     */
    public boolean hasKeyID() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional uint32 KeyID = 1;</code>
     */
    public int getKeyID() {
      return keyID_;
    }

    public static final int KEYDATA_FIELD_NUMBER = 2;
    private com.google.protobuf.ByteString keyData_;
    /**
     * <code>optional bytes KeyData = 2;</code>
     */
    public boolean hasKeyData() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional bytes KeyData = 2;</code>
     */
    public com.google.protobuf.ByteString getKeyData() {
      return keyData_;
    }

    private void initFields() {
      keyID_ = 0;
      keyData_ = com.google.protobuf.ByteString.EMPTY;
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
        output.writeUInt32(1, keyID_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, keyData_);
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
          .computeUInt32Size(1, keyID_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, keyData_);
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

    public static ICloud.MBSKey parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSKey parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSKey parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSKey parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSKey parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSKey parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static ICloud.MBSKey parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static ICloud.MBSKey parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static ICloud.MBSKey parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSKey parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(ICloud.MBSKey prototype) {
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
     * Protobuf type {@code MBSKey}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:MBSKey)
        ICloud.MBSKeyOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ICloud.internal_static_MBSKey_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ICloud.internal_static_MBSKey_fieldAccessorTable
            .ensureFieldAccessorsInitialized(ICloud.MBSKey.class, ICloud.MBSKey.Builder.class);
      }

      // Construct using Icloud.MBSKey.newBuilder()
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
        keyID_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        keyData_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ICloud.internal_static_MBSKey_descriptor;
      }

      public ICloud.MBSKey getDefaultInstanceForType() {
        return ICloud.MBSKey.getDefaultInstance();
      }

      public ICloud.MBSKey build() {
        ICloud.MBSKey result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ICloud.MBSKey buildPartial() {
        ICloud.MBSKey result = new ICloud.MBSKey(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.keyID_ = keyID_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.keyData_ = keyData_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ICloud.MBSKey) {
          return mergeFrom((ICloud.MBSKey)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ICloud.MBSKey other) {
        if (other == ICloud.MBSKey.getDefaultInstance()) return this;
        if (other.hasKeyID()) {
          setKeyID(other.getKeyID());
        }
        if (other.hasKeyData()) {
          setKeyData(other.getKeyData());
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
        ICloud.MBSKey parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ICloud.MBSKey) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int keyID_ ;
      /**
       * <code>optional uint32 KeyID = 1;</code>
       */
      public boolean hasKeyID() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional uint32 KeyID = 1;</code>
       */
      public int getKeyID() {
        return keyID_;
      }
      /**
       * <code>optional uint32 KeyID = 1;</code>
       */
      public Builder setKeyID(int value) {
        bitField0_ |= 0x00000001;
        keyID_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 KeyID = 1;</code>
       */
      public Builder clearKeyID() {
        bitField0_ = (bitField0_ & ~0x00000001);
        keyID_ = 0;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString keyData_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes KeyData = 2;</code>
       */
      public boolean hasKeyData() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional bytes KeyData = 2;</code>
       */
      public com.google.protobuf.ByteString getKeyData() {
        return keyData_;
      }
      /**
       * <code>optional bytes KeyData = 2;</code>
       */
      public Builder setKeyData(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        keyData_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes KeyData = 2;</code>
       */
      public Builder clearKeyData() {
        bitField0_ = (bitField0_ & ~0x00000002);
        keyData_ = getDefaultInstance().getKeyData();
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:MBSKey)
    }

    static {
      defaultInstance = new MBSKey(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:MBSKey)
  }

  public interface MBSKeySetOrBuilder extends
      // @@protoc_insertion_point(interface_extends:MBSKeySet)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>repeated .MBSKey Key = 1;</code>
     */
    java.util.List<ICloud.MBSKey> 
        getKeyList();
    /**
     * <code>repeated .MBSKey Key = 1;</code>
     */
    ICloud.MBSKey getKey(int index);
    /**
     * <code>repeated .MBSKey Key = 1;</code>
     */
    int getKeyCount();
    /**
     * <code>repeated .MBSKey Key = 1;</code>
     */
    java.util.List<? extends ICloud.MBSKeyOrBuilder> 
        getKeyOrBuilderList();
    /**
     * <code>repeated .MBSKey Key = 1;</code>
     */
    ICloud.MBSKeyOrBuilder getKeyOrBuilder(
        int index);
  }
  /**
   * Protobuf type {@code MBSKeySet}
   */
  public static final class MBSKeySet extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:MBSKeySet)
      MBSKeySetOrBuilder {
    // Use MBSKeySet.newBuilder() to construct.
    private MBSKeySet(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private MBSKeySet(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final MBSKeySet defaultInstance;
    public static MBSKeySet getDefaultInstance() {
      return defaultInstance;
    }

    public MBSKeySet getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private MBSKeySet(
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
              if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
                key_ = new java.util.ArrayList<ICloud.MBSKey>();
                mutable_bitField0_ |= 0x00000001;
              }
              key_.add(input.readMessage(ICloud.MBSKey.PARSER, extensionRegistry));
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
        if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
          key_ = java.util.Collections.unmodifiableList(key_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return ICloud.internal_static_MBSKeySet_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ICloud.internal_static_MBSKeySet_fieldAccessorTable
          .ensureFieldAccessorsInitialized(ICloud.MBSKeySet.class, ICloud.MBSKeySet.Builder.class);
    }

    public static com.google.protobuf.Parser<MBSKeySet> PARSER =
        new com.google.protobuf.AbstractParser<MBSKeySet>() {
      public MBSKeySet parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MBSKeySet(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<MBSKeySet> getParserForType() {
      return PARSER;
    }

    public static final int KEY_FIELD_NUMBER = 1;
    private java.util.List<ICloud.MBSKey> key_;
    /**
     * <code>repeated .MBSKey Key = 1;</code>
     */
    public java.util.List<ICloud.MBSKey> getKeyList() {
      return key_;
    }
    /**
     * <code>repeated .MBSKey Key = 1;</code>
     */
    public java.util.List<? extends ICloud.MBSKeyOrBuilder> 
        getKeyOrBuilderList() {
      return key_;
    }
    /**
     * <code>repeated .MBSKey Key = 1;</code>
     */
    public int getKeyCount() {
      return key_.size();
    }
    /**
     * <code>repeated .MBSKey Key = 1;</code>
     */
    public ICloud.MBSKey getKey(int index) {
      return key_.get(index);
    }
    /**
     * <code>repeated .MBSKey Key = 1;</code>
     */
    public ICloud.MBSKeyOrBuilder getKeyOrBuilder(
        int index) {
      return key_.get(index);
    }

    private void initFields() {
      key_ = java.util.Collections.emptyList();
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
      for (int i = 0; i < key_.size(); i++) {
        output.writeMessage(1, key_.get(i));
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      for (int i = 0; i < key_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, key_.get(i));
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

    public static ICloud.MBSKeySet parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSKeySet parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSKeySet parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSKeySet parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSKeySet parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSKeySet parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static ICloud.MBSKeySet parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static ICloud.MBSKeySet parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static ICloud.MBSKeySet parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSKeySet parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(ICloud.MBSKeySet prototype) {
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
     * Protobuf type {@code MBSKeySet}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:MBSKeySet)
        ICloud.MBSKeySetOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ICloud.internal_static_MBSKeySet_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ICloud.internal_static_MBSKeySet_fieldAccessorTable
            .ensureFieldAccessorsInitialized(ICloud.MBSKeySet.class, ICloud.MBSKeySet.Builder.class);
      }

      // Construct using Icloud.MBSKeySet.newBuilder()
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
          getKeyFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (keyBuilder_ == null) {
          key_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          keyBuilder_.clear();
        }
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ICloud.internal_static_MBSKeySet_descriptor;
      }

      public ICloud.MBSKeySet getDefaultInstanceForType() {
        return ICloud.MBSKeySet.getDefaultInstance();
      }

      public ICloud.MBSKeySet build() {
        ICloud.MBSKeySet result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ICloud.MBSKeySet buildPartial() {
        ICloud.MBSKeySet result = new ICloud.MBSKeySet(this);
        int from_bitField0_ = bitField0_;
        if (keyBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001)) {
            key_ = java.util.Collections.unmodifiableList(key_);
            bitField0_ = (bitField0_ & ~0x00000001);
          }
          result.key_ = key_;
        } else {
          result.key_ = keyBuilder_.build();
        }
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ICloud.MBSKeySet) {
          return mergeFrom((ICloud.MBSKeySet)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ICloud.MBSKeySet other) {
        if (other == ICloud.MBSKeySet.getDefaultInstance()) return this;
        if (keyBuilder_ == null) {
          if (!other.key_.isEmpty()) {
            if (key_.isEmpty()) {
              key_ = other.key_;
              bitField0_ = (bitField0_ & ~0x00000001);
            } else {
              ensureKeyIsMutable();
              key_.addAll(other.key_);
            }
            onChanged();
          }
        } else {
          if (!other.key_.isEmpty()) {
            if (keyBuilder_.isEmpty()) {
              keyBuilder_.dispose();
              keyBuilder_ = null;
              key_ = other.key_;
              bitField0_ = (bitField0_ & ~0x00000001);
              keyBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getKeyFieldBuilder() : null;
            } else {
              keyBuilder_.addAllMessages(other.key_);
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
        ICloud.MBSKeySet parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ICloud.MBSKeySet) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.util.List<ICloud.MBSKey> key_ =
        java.util.Collections.emptyList();
      private void ensureKeyIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          key_ = new java.util.ArrayList<ICloud.MBSKey>(key_);
          bitField0_ |= 0x00000001;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          ICloud.MBSKey, ICloud.MBSKey.Builder, ICloud.MBSKeyOrBuilder> keyBuilder_;

      /**
       * <code>repeated .MBSKey Key = 1;</code>
       */
      public java.util.List<ICloud.MBSKey> getKeyList() {
        if (keyBuilder_ == null) {
          return java.util.Collections.unmodifiableList(key_);
        } else {
          return keyBuilder_.getMessageList();
        }
      }
      /**
       * <code>repeated .MBSKey Key = 1;</code>
       */
      public int getKeyCount() {
        if (keyBuilder_ == null) {
          return key_.size();
        } else {
          return keyBuilder_.getCount();
        }
      }
      /**
       * <code>repeated .MBSKey Key = 1;</code>
       */
      public ICloud.MBSKey getKey(int index) {
        if (keyBuilder_ == null) {
          return key_.get(index);
        } else {
          return keyBuilder_.getMessage(index);
        }
      }
      /**
       * <code>repeated .MBSKey Key = 1;</code>
       */
      public Builder setKey(
          int index, ICloud.MBSKey value) {
        if (keyBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureKeyIsMutable();
          key_.set(index, value);
          onChanged();
        } else {
          keyBuilder_.setMessage(index, value);
        }
        return this;
      }
      /**
       * <code>repeated .MBSKey Key = 1;</code>
       */
      public Builder setKey(
          int index, ICloud.MBSKey.Builder builderForValue) {
        if (keyBuilder_ == null) {
          ensureKeyIsMutable();
          key_.set(index, builderForValue.build());
          onChanged();
        } else {
          keyBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .MBSKey Key = 1;</code>
       */
      public Builder addKey(ICloud.MBSKey value) {
        if (keyBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureKeyIsMutable();
          key_.add(value);
          onChanged();
        } else {
          keyBuilder_.addMessage(value);
        }
        return this;
      }
      /**
       * <code>repeated .MBSKey Key = 1;</code>
       */
      public Builder addKey(
          int index, ICloud.MBSKey value) {
        if (keyBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureKeyIsMutable();
          key_.add(index, value);
          onChanged();
        } else {
          keyBuilder_.addMessage(index, value);
        }
        return this;
      }
      /**
       * <code>repeated .MBSKey Key = 1;</code>
       */
      public Builder addKey(
          ICloud.MBSKey.Builder builderForValue) {
        if (keyBuilder_ == null) {
          ensureKeyIsMutable();
          key_.add(builderForValue.build());
          onChanged();
        } else {
          keyBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .MBSKey Key = 1;</code>
       */
      public Builder addKey(
          int index, ICloud.MBSKey.Builder builderForValue) {
        if (keyBuilder_ == null) {
          ensureKeyIsMutable();
          key_.add(index, builderForValue.build());
          onChanged();
        } else {
          keyBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .MBSKey Key = 1;</code>
       */
      public Builder addAllKey(
          java.lang.Iterable<? extends ICloud.MBSKey> values) {
        if (keyBuilder_ == null) {
          ensureKeyIsMutable();
          com.google.protobuf.AbstractMessageLite.Builder.addAll(
              values, key_);
          onChanged();
        } else {
          keyBuilder_.addAllMessages(values);
        }
        return this;
      }
      /**
       * <code>repeated .MBSKey Key = 1;</code>
       */
      public Builder clearKey() {
        if (keyBuilder_ == null) {
          key_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
          onChanged();
        } else {
          keyBuilder_.clear();
        }
        return this;
      }
      /**
       * <code>repeated .MBSKey Key = 1;</code>
       */
      public Builder removeKey(int index) {
        if (keyBuilder_ == null) {
          ensureKeyIsMutable();
          key_.remove(index);
          onChanged();
        } else {
          keyBuilder_.remove(index);
        }
        return this;
      }
      /**
       * <code>repeated .MBSKey Key = 1;</code>
       */
      public ICloud.MBSKey.Builder getKeyBuilder(
          int index) {
        return getKeyFieldBuilder().getBuilder(index);
      }
      /**
       * <code>repeated .MBSKey Key = 1;</code>
       */
      public ICloud.MBSKeyOrBuilder getKeyOrBuilder(
          int index) {
        if (keyBuilder_ == null) {
          return key_.get(index);  } else {
          return keyBuilder_.getMessageOrBuilder(index);
        }
      }
      /**
       * <code>repeated .MBSKey Key = 1;</code>
       */
      public java.util.List<? extends ICloud.MBSKeyOrBuilder> 
           getKeyOrBuilderList() {
        if (keyBuilder_ != null) {
          return keyBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(key_);
        }
      }
      /**
       * <code>repeated .MBSKey Key = 1;</code>
       */
      public ICloud.MBSKey.Builder addKeyBuilder() {
        return getKeyFieldBuilder().addBuilder(ICloud.MBSKey.getDefaultInstance());
      }
      /**
       * <code>repeated .MBSKey Key = 1;</code>
       */
      public ICloud.MBSKey.Builder addKeyBuilder(
          int index) {
        return getKeyFieldBuilder().addBuilder(index, ICloud.MBSKey.getDefaultInstance());
      }
      /**
       * <code>repeated .MBSKey Key = 1;</code>
       */
      public java.util.List<ICloud.MBSKey.Builder> 
           getKeyBuilderList() {
        return getKeyFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          ICloud.MBSKey, ICloud.MBSKey.Builder, ICloud.MBSKeyOrBuilder> 
          getKeyFieldBuilder() {
        if (keyBuilder_ == null) {
          keyBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              ICloud.MBSKey, ICloud.MBSKey.Builder, ICloud.MBSKeyOrBuilder>(
                  key_,
                  ((bitField0_ & 0x00000001) == 0x00000001),
                  getParentForChildren(),
                  isClean());
          key_ = null;
        }
        return keyBuilder_;
      }

      // @@protoc_insertion_point(builder_scope:MBSKeySet)
    }

    static {
      defaultInstance = new MBSKeySet(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:MBSKeySet)
  }

  public interface MBSFileExtendedAttributeOrBuilder extends
      // @@protoc_insertion_point(interface_extends:MBSFileExtendedAttribute)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string Name = 1;</code>
     */
    boolean hasName();
    /**
     * <code>optional string Name = 1;</code>
     */
    java.lang.String getName();
    /**
     * <code>optional string Name = 1;</code>
     */
    com.google.protobuf.ByteString
        getNameBytes();

    /**
     * <code>optional bytes Value = 2;</code>
     */
    boolean hasValue();
    /**
     * <code>optional bytes Value = 2;</code>
     */
    com.google.protobuf.ByteString getValue();
  }
  /**
   * Protobuf type {@code MBSFileExtendedAttribute}
   */
  public static final class MBSFileExtendedAttribute extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:MBSFileExtendedAttribute)
      MBSFileExtendedAttributeOrBuilder {
    // Use MBSFileExtendedAttribute.newBuilder() to construct.
    private MBSFileExtendedAttribute(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private MBSFileExtendedAttribute(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final MBSFileExtendedAttribute defaultInstance;
    public static MBSFileExtendedAttribute getDefaultInstance() {
      return defaultInstance;
    }

    public MBSFileExtendedAttribute getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private MBSFileExtendedAttribute(
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
              name_ = bs;
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              value_ = input.readBytes();
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
      return ICloud.internal_static_MBSFileExtendedAttribute_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ICloud.internal_static_MBSFileExtendedAttribute_fieldAccessorTable
          .ensureFieldAccessorsInitialized(ICloud.MBSFileExtendedAttribute.class, ICloud.MBSFileExtendedAttribute.Builder.class);
    }

    public static com.google.protobuf.Parser<MBSFileExtendedAttribute> PARSER =
        new com.google.protobuf.AbstractParser<MBSFileExtendedAttribute>() {
      public MBSFileExtendedAttribute parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MBSFileExtendedAttribute(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<MBSFileExtendedAttribute> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int NAME_FIELD_NUMBER = 1;
    private java.lang.Object name_;
    /**
     * <code>optional string Name = 1;</code>
     */
    public boolean hasName() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional string Name = 1;</code>
     */
    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          name_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string Name = 1;</code>
     */
    public com.google.protobuf.ByteString
        getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int VALUE_FIELD_NUMBER = 2;
    private com.google.protobuf.ByteString value_;
    /**
     * <code>optional bytes Value = 2;</code>
     */
    public boolean hasValue() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional bytes Value = 2;</code>
     */
    public com.google.protobuf.ByteString getValue() {
      return value_;
    }

    private void initFields() {
      name_ = "";
      value_ = com.google.protobuf.ByteString.EMPTY;
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
        output.writeBytes(1, getNameBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, value_);
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
          .computeBytesSize(1, getNameBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, value_);
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

    public static ICloud.MBSFileExtendedAttribute parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSFileExtendedAttribute parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSFileExtendedAttribute parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ICloud.MBSFileExtendedAttribute parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ICloud.MBSFileExtendedAttribute parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSFileExtendedAttribute parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static ICloud.MBSFileExtendedAttribute parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static ICloud.MBSFileExtendedAttribute parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static ICloud.MBSFileExtendedAttribute parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ICloud.MBSFileExtendedAttribute parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(ICloud.MBSFileExtendedAttribute prototype) {
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
     * Protobuf type {@code MBSFileExtendedAttribute}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:MBSFileExtendedAttribute)
        ICloud.MBSFileExtendedAttributeOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ICloud.internal_static_MBSFileExtendedAttribute_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ICloud.internal_static_MBSFileExtendedAttribute_fieldAccessorTable
            .ensureFieldAccessorsInitialized(ICloud.MBSFileExtendedAttribute.class, ICloud.MBSFileExtendedAttribute.Builder.class);
      }

      // Construct using Icloud.MBSFileExtendedAttribute.newBuilder()
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
        name_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        value_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ICloud.internal_static_MBSFileExtendedAttribute_descriptor;
      }

      public ICloud.MBSFileExtendedAttribute getDefaultInstanceForType() {
        return ICloud.MBSFileExtendedAttribute.getDefaultInstance();
      }

      public ICloud.MBSFileExtendedAttribute build() {
        ICloud.MBSFileExtendedAttribute result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ICloud.MBSFileExtendedAttribute buildPartial() {
        ICloud.MBSFileExtendedAttribute result = new ICloud.MBSFileExtendedAttribute(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.name_ = name_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.value_ = value_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ICloud.MBSFileExtendedAttribute) {
          return mergeFrom((ICloud.MBSFileExtendedAttribute)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ICloud.MBSFileExtendedAttribute other) {
        if (other == ICloud.MBSFileExtendedAttribute.getDefaultInstance()) return this;
        if (other.hasName()) {
          bitField0_ |= 0x00000001;
          name_ = other.name_;
          onChanged();
        }
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
        ICloud.MBSFileExtendedAttribute parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ICloud.MBSFileExtendedAttribute) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object name_ = "";
      /**
       * <code>optional string Name = 1;</code>
       */
      public boolean hasName() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional string Name = 1;</code>
       */
      public java.lang.String getName() {
        java.lang.Object ref = name_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            name_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string Name = 1;</code>
       */
      public com.google.protobuf.ByteString
          getNameBytes() {
        java.lang.Object ref = name_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          name_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string Name = 1;</code>
       */
      public Builder setName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string Name = 1;</code>
       */
      public Builder clearName() {
        bitField0_ = (bitField0_ & ~0x00000001);
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }
      /**
       * <code>optional string Name = 1;</code>
       */
      public Builder setNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString value_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes Value = 2;</code>
       */
      public boolean hasValue() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional bytes Value = 2;</code>
       */
      public com.google.protobuf.ByteString getValue() {
        return value_;
      }
      /**
       * <code>optional bytes Value = 2;</code>
       */
      public Builder setValue(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        value_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes Value = 2;</code>
       */
      public Builder clearValue() {
        bitField0_ = (bitField0_ & ~0x00000002);
        value_ = getDefaultInstance().getValue();
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:MBSFileExtendedAttribute)
    }

    static {
      defaultInstance = new MBSFileExtendedAttribute(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:MBSFileExtendedAttribute)
  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MBSAccount_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MBSAccount_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MBSBackup_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MBSBackup_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MBSBackupAttributes_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MBSBackupAttributes_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MBSFile_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MBSFile_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MBSFileAttributes_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MBSFileAttributes_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MBSSnapshot_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MBSSnapshot_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MBSSnapshotAttributes_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MBSSnapshotAttributes_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MBSSnapshotID_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MBSSnapshotID_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MBSFileAuthToken_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MBSFileAuthToken_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MBSFileAuthTokens_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MBSFileAuthTokens_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MBSFileID_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MBSFileID_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MBSKey_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MBSKey_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MBSKeySet_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MBSKeySet_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MBSFileExtendedAttribute_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MBSFileExtendedAttribute_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\014icloud.proto\"3\n\nMBSAccount\022\021\n\tAccountI" +
      "D\030\001 \001(\t\022\022\n\nbackupUDID\030\002 \003(\014\"\226\001\n\tMBSBacku" +
      "p\022\022\n\nbackupUDID\030\001 \001(\014\022\021\n\tQuotaUsed\030\002 \001(\004" +
      "\022\036\n\010Snapshot\030\003 \003(\0132\014.MBSSnapshot\022(\n\nAttr" +
      "ibutes\030\004 \001(\0132\024.MBSBackupAttributes\022\030\n\020Ke" +
      "ysLastModified\030\005 \001(\004\"\230\001\n\023MBSBackupAttrib" +
      "utes\022\023\n\013DeviceClass\030\001 \001(\t\022\023\n\013ProductType" +
      "\030\002 \001(\t\022\024\n\014SerialNumber\030\003 \001(\t\022\023\n\013DeviceCo" +
      "lor\030\004 \001(\t\022\025\n\rHardwareModel\030\005 \001(\t\022\025\n\rMark" +
      "etingName\030\006 \001(\t\"\210\001\n\007MBSFile\022\016\n\006FileID\030\001 ",
      "\001(\014\022\016\n\006Domain\030\002 \001(\t\022\024\n\014RelativePath\030\003 \001(" +
      "\t\022\021\n\tSignature\030\004 \001(\014\022\014\n\004Size\030\005 \001(\004\022&\n\nAt" +
      "tributes\030\006 \001(\0132\022.MBSFileAttributes\"\323\002\n\021M" +
      "BSFileAttributes\022\020\n\010KeybagID\030\001 \001(\r\022\016\n\006Ta" +
      "rget\030\002 \001(\t\022\025\n\rEncryptionKey\030\003 \001(\014\022\023\n\013Ino" +
      "deNumber\030\004 \001(\004\022\014\n\004Mode\030\005 \001(\r\022\016\n\006UserID\030\006" +
      " \001(\r\022\017\n\007GroupID\030\007 \001(\r\022\024\n\014LastModified\030\010 " +
      "\001(\004\022\030\n\020LastStatusChange\030\t \001(\004\022\r\n\005Birth\030\n" +
      " \001(\004\022\027\n\017ProtectionClass\030\014 \001(\r\0224\n\021Extende" +
      "dAttribute\030\r \001(\0132\031.MBSFileExtendedAttrib",
      "ute\022\034\n\024EncryptionKeyVersion\030\016 \001(\r\022\025\n\rDec" +
      "ryptedSize\030\017 \001(\004\"\215\001\n\013MBSSnapshot\022\022\n\nSnap" +
      "shotID\030\001 \001(\r\022\025\n\rQuotaReserved\030\002 \001(\004\022\024\n\014L" +
      "astModified\030\003 \001(\004\022*\n\nAttributes\030\005 \001(\0132\026." +
      "MBSSnapshotAttributes\022\021\n\tCommitted\030\006 \001(\004" +
      "\"\251\001\n\025MBSSnapshotAttributes\022\022\n\nDeviceName" +
      "\030\001 \001(\t\022\026\n\016ProductVersion\030\002 \001(\t\022\024\n\014BuildV" +
      "ersion\030\003 \001(\t\022\020\n\010KeybagID\030\004 \001(\r\022\022\n\nKeybag" +
      "UUID\030\005 \001(\014\022\024\n\014BackupReason\030\006 \001(\005\022\022\n\nBack" +
      "upType\030\007 \001(\005\"#\n\rMBSSnapshotID\022\022\n\nSnapsho",
      "tID\030\001 \001(\r\"5\n\020MBSFileAuthToken\022\016\n\006FileID\030" +
      "\001 \002(\014\022\021\n\tAuthToken\030\002 \001(\t\"6\n\021MBSFileAuthT" +
      "okens\022!\n\006tokens\030\001 \003(\0132\021.MBSFileAuthToken" +
      "\"\033\n\tMBSFileID\022\016\n\006FileID\030\001 \002(\014\"(\n\006MBSKey\022" +
      "\r\n\005KeyID\030\001 \001(\r\022\017\n\007KeyData\030\002 \001(\014\"!\n\tMBSKe" +
      "ySet\022\024\n\003Key\030\001 \003(\0132\007.MBSKey\"7\n\030MBSFileExt" +
      "endedAttribute\022\014\n\004Name\030\001 \001(\t\022\r\n\005Value\030\002 " +
      "\001(\014"
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
    internal_static_MBSAccount_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_MBSAccount_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_MBSAccount_descriptor,
        new java.lang.String[] { "AccountID", "BackupUDID", });
    internal_static_MBSBackup_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_MBSBackup_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_MBSBackup_descriptor,
        new java.lang.String[] { "BackupUDID", "QuotaUsed", "Snapshot", "Attributes", "KeysLastModified", });
    internal_static_MBSBackupAttributes_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_MBSBackupAttributes_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_MBSBackupAttributes_descriptor,
        new java.lang.String[] { "DeviceClass", "ProductType", "SerialNumber", "DeviceColor", "HardwareModel", "MarketingName", });
    internal_static_MBSFile_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_MBSFile_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_MBSFile_descriptor,
        new java.lang.String[] { "FileID", "Domain", "RelativePath", "Signature", "Size", "Attributes", });
    internal_static_MBSFileAttributes_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_MBSFileAttributes_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_MBSFileAttributes_descriptor,
        new java.lang.String[] { "KeybagID", "Target", "EncryptionKey", "InodeNumber", "Mode", "UserID", "GroupID", "LastModified", "LastStatusChange", "Birth", "ProtectionClass", "ExtendedAttribute", "EncryptionKeyVersion", "DecryptedSize", });
    internal_static_MBSSnapshot_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_MBSSnapshot_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_MBSSnapshot_descriptor,
        new java.lang.String[] { "SnapshotID", "QuotaReserved", "LastModified", "Attributes", "Committed", });
    internal_static_MBSSnapshotAttributes_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_MBSSnapshotAttributes_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_MBSSnapshotAttributes_descriptor,
        new java.lang.String[] { "DeviceName", "ProductVersion", "BuildVersion", "KeybagID", "KeybagUUID", "BackupReason", "BackupType", });
    internal_static_MBSSnapshotID_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_MBSSnapshotID_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_MBSSnapshotID_descriptor,
        new java.lang.String[] { "SnapshotID", });
    internal_static_MBSFileAuthToken_descriptor =
      getDescriptor().getMessageTypes().get(8);
    internal_static_MBSFileAuthToken_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_MBSFileAuthToken_descriptor,
        new java.lang.String[] { "FileID", "AuthToken", });
    internal_static_MBSFileAuthTokens_descriptor =
      getDescriptor().getMessageTypes().get(9);
    internal_static_MBSFileAuthTokens_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_MBSFileAuthTokens_descriptor,
        new java.lang.String[] { "Tokens", });
    internal_static_MBSFileID_descriptor =
      getDescriptor().getMessageTypes().get(10);
    internal_static_MBSFileID_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_MBSFileID_descriptor,
        new java.lang.String[] { "FileID", });
    internal_static_MBSKey_descriptor =
      getDescriptor().getMessageTypes().get(11);
    internal_static_MBSKey_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_MBSKey_descriptor,
        new java.lang.String[] { "KeyID", "KeyData", });
    internal_static_MBSKeySet_descriptor =
      getDescriptor().getMessageTypes().get(12);
    internal_static_MBSKeySet_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_MBSKeySet_descriptor,
        new java.lang.String[] { "Key", });
    internal_static_MBSFileExtendedAttribute_descriptor =
      getDescriptor().getMessageTypes().get(13);
    internal_static_MBSFileExtendedAttribute_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_MBSFileExtendedAttribute_descriptor,
        new java.lang.String[] { "Name", "Value", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}