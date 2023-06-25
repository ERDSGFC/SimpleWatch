package com.qin.simplewatch;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

@Data
//@Builder
public class JsonTest extends Super{
    long id;
    int[] a;
    LocalDateTime time;

}
@Data
class Super{
    Map<String, String> map;
}
