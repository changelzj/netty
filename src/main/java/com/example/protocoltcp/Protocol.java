package com.example.protocoltcp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Protocol {
    private int len;
    private byte[] content;
}
