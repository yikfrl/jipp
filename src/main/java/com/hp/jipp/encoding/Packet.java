package com.hp.jipp.encoding;

import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.hp.jipp.model.Operation;
import com.hp.jipp.model.Status;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A request packet as specified in RFC2910.
 */
@AutoValue
public abstract class Packet {
    /** Default version number to be sent in a packet (0x0101 for IPP 1.1) */
    public static final int DEFAULT_VERSION_NUMBER = 0x0101;
    private static final byte[] EMPTY_DATA = new byte[0];

    abstract public int getVersionNumber();

    /**
     * Return this packet's operation code (for a request) or status code (for a response).
     */
    abstract public int getCode();

    /** Return this request packet's operation */
    public Operation getOperation() { return Operation.toOperation(getCode()); }

    /** Return this response packet's status */
    public Status getStatus() { return Status.toStatus(getCode()); }

    /**
     * Return the request ID for this packet
     */
    abstract public int getRequestId();

    /**
     * Return the attribute groups in this packet
     */
    abstract public ImmutableList<AttributeGroup> getAttributeGroups();

    /** Returns the first attribute with the specified delimiter */
    public Optional<AttributeGroup> findAttributeGroup(final Tag delimiter) {
        for (AttributeGroup group : getAttributeGroups()) {
            if (group.getStartTag() == delimiter) return Optional.of(group);
        }
        return Optional.absent();
    }

    /**
     * Return the packet's data field (bytes found after all attributes)
     */
    @SuppressWarnings("mutable")
    abstract public byte[] getData();

    /** Construct and return a builder for creating packets */
    public static Builder builder() {
        return new AutoValue_Packet.Builder().setVersionNumber(DEFAULT_VERSION_NUMBER)
                .setAttributeGroups().setData(EMPTY_DATA);
    }

    /** Construct and return a builder based on an existing packet */
    public static Builder builder(Packet source) {
        return new AutoValue_Packet.Builder(source);
    }

    /**
     * Construct a packet containing the default version number and the specified operation
     * and request ID
     */
    public static Builder builder(Operation operation, int requestId) {
        return builder().setOperation(operation).setRequestId(requestId);
    }

    /**
     * Construct and return a complete packet
     */
    public static Packet create(Operation operation, int requestId, AttributeGroup... groups) {
        return builder(operation, requestId).setAttributeGroups(groups).build();
    }

    /** Write the contents of this object to the output stream as per RFC2910 */
    public void write(DataOutputStream out) throws IOException {
        out.writeShort(getVersionNumber());
        out.writeShort(getCode());
        out.writeInt(getRequestId());
        for (AttributeGroup group : getAttributeGroups()) {
            group.write(out);
        }
        out.writeByte(Tag.EndOfAttributes.getValue());
        out.write(getData());
    }

    /** Read the contents of the input stream, returning a parsed Packet or throwing an exception */
    public static Packet read(DataInputStream in) throws IOException {
        Packet.Builder builder = builder().setVersionNumber(in.readShort())
                .setCode(in.readShort()).setRequestId(in.readInt());
        ImmutableList.Builder<AttributeGroup> attributeGroupsBuilder =
                new ImmutableList.Builder<>();

        boolean moreAttributes = true;
        while(moreAttributes) {
            Tag tag = Tag.read(in);
            if (tag == Tag.EndOfAttributes) {
                if (in.available() > 0) {
                    byte data[] = new byte[in.available()];
                    int size = in.read(data);
                    if (size != data.length) throw new ParseError("Failed to read " + data.length + ": " + size);
                    builder.setData(data);
                }
                moreAttributes = false;
            } else if (tag.isDelimiter()) {
                AttributeGroup attributeGroup = AttributeGroup.read(tag, in);
                attributeGroupsBuilder.add(attributeGroup);
            } else {
                throw new ParseError("Illegal delimiter tag " + tag);
            }
        }
        builder.setAttributeGroups(attributeGroupsBuilder.build());
        return builder.build();
    }

    @AutoValue.Builder
    abstract public static class Builder {
        abstract public Builder setVersionNumber(int versionNumber);
        abstract public Builder setCode(int code);
        public Builder setOperation(Operation operation) {
            return setCode(operation.getCode());
        }
        abstract public Builder setRequestId(int requestId);
        abstract public Builder setAttributeGroups(Iterable<AttributeGroup> groups);
        abstract public Builder setAttributeGroups(AttributeGroup... groups);
        abstract public Builder setData(byte[] data);
        abstract public Packet build();
    }

    @Override
    public final String toString() {
        return "Packet{v=x" + Integer.toHexString(getVersionNumber()) +
                ", code=" + (Operation.isKnown(getCode()) ? Operation.toOperation(getCode()) : Status.toStatus(getCode())) +
                ", rid=x" + Integer.toHexString(getRequestId()) +
                ", a=" + getAttributeGroups() +
                (getData().length == 0 ? "" : ", dlen=" + getData().length) +
                "}";
    }
}
