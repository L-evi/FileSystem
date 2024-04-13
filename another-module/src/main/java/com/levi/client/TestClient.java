package com.levi.client;

import com.dtflys.forest.annotation.Address;
import com.levi.api.TestApi;

@Address(host = "localhost", port = "15000")
public interface TestClient extends TestApi {
}
