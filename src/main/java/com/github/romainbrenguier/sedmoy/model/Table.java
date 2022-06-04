package com.github.romainbrenguier.sedmoy.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
//    defaultImpl = MyInterfaceImpl.class
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = DataTable.class, name = "data"),
    @JsonSubTypes.Type(value = FormulaTable.class, name = "formula")
})
public interface Table {
}
