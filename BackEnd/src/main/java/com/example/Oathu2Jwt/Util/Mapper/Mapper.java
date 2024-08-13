package com.example.Oathu2Jwt.Util.Mapper;

public interface Mapper<A,B> {
    B mapTo(A a);

    A mapFrom(B b );
}
