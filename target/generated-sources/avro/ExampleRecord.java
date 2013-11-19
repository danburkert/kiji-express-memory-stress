/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
@SuppressWarnings("all")
/** Example record. */
@org.apache.avro.specific.AvroGenerated
public class ExampleRecord extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ExampleRecord\",\"doc\":\"Example record.\",\"fields\":[{\"name\":\"id\",\"type\":\"string\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.CharSequence id;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use {@link \#newBuilder()}. 
   */
  public ExampleRecord() {}

  /**
   * All-args constructor.
   */
  public ExampleRecord(java.lang.CharSequence id) {
    this.id = id;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.lang.CharSequence)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'id' field.
   */
  public java.lang.CharSequence getId() {
    return id;
  }

  /**
   * Sets the value of the 'id' field.
   * @param value the value to set.
   */
  public void setId(java.lang.CharSequence value) {
    this.id = value;
  }

  /** Creates a new ExampleRecord RecordBuilder */
  public static ExampleRecord.Builder newBuilder() {
    return new ExampleRecord.Builder();
  }
  
  /** Creates a new ExampleRecord RecordBuilder by copying an existing Builder */
  public static ExampleRecord.Builder newBuilder(ExampleRecord.Builder other) {
    return new ExampleRecord.Builder(other);
  }
  
  /** Creates a new ExampleRecord RecordBuilder by copying an existing ExampleRecord instance */
  public static ExampleRecord.Builder newBuilder(ExampleRecord other) {
    return new ExampleRecord.Builder(other);
  }
  
  /**
   * RecordBuilder for ExampleRecord instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<ExampleRecord>
    implements org.apache.avro.data.RecordBuilder<ExampleRecord> {

    private java.lang.CharSequence id;

    /** Creates a new Builder */
    private Builder() {
      super(ExampleRecord.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(ExampleRecord.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
    }
    
    /** Creates a Builder by copying an existing ExampleRecord instance */
    private Builder(ExampleRecord other) {
            super(ExampleRecord.SCHEMA$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
    }

    /** Gets the value of the 'id' field */
    public java.lang.CharSequence getId() {
      return id;
    }
    
    /** Sets the value of the 'id' field */
    public ExampleRecord.Builder setId(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'id' field has been set */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'id' field */
    public ExampleRecord.Builder clearId() {
      id = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    @Override
    public ExampleRecord build() {
      try {
        ExampleRecord record = new ExampleRecord();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.CharSequence) defaultValue(fields()[0]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}