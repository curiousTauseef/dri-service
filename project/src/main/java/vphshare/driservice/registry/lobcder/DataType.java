package vphshare.driservice.registry.lobcder;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlEnum
public enum DataType {
    @XmlEnumValue("logical.file")
    FILE,
    @XmlEnumValue("logical.folder")
    DIRECTORY
}
