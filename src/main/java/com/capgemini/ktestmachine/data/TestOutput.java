package com.capgemini.ktestmachine.data;

import java.util.Map;

public interface TestOutput extends Test {

    Map<String, Object> getDataOutput();
}
