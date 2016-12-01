package com.nextmatch.vero.jsonapiadapter.model;

/**
 * Gson 에서 제공하는 TypeAdapterFactory 를 사용하려면 read, write 에서 사용하는 type 이 같아야하는데
 * 원하는 기능을 모두 제공하려면 read 는 Adapter 를 write 는 Resource 를 사용해야 해서 Interface 를 추가함.
 * @author vero
 * @since 2016. 11. 30.
 */
public interface ResourceProvider {

}
