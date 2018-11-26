package com.conoftherings.controller;

import org.assertj.core.api.AbstractAssert;
import org.springframework.http.ResponseEntity;


public class Assertions extends org.assertj.core.api.Assertions {

    /**
     * Create an assertion for a Spring {@link ResponseEntity}.
     *
     * @param responseEntity The response entity.
     * @return The created assertion object.
     */
    public static AssertResponseEntity assertThat(ResponseEntity<?> responseEntity) {
        return new AssertResponseEntity(responseEntity);
    }

    public static class AssertResponseEntity extends AbstractAssert<AssertResponseEntity, ResponseEntity<?>> {

        public AssertResponseEntity(ResponseEntity<?> responseEntity) {
            super(responseEntity, AssertResponseEntity.class);
        }

        /**
         * Assert the status code is the given value.
         *
         * @param statusCode The expected status code.
         */
        public AssertResponseEntity statusCodeIs(int statusCode) {
            isNotNull();
            assertThat(actual.getStatusCode().value()).isEqualTo(statusCode);
            return this;
        }

        /**
         * Assert the status code is OK/OK.
         */
        public AssertResponseEntity statusCodeIsOK() {
            return isNotNull().statusCodeIs(200);
        }

        /**
         * Assert the status code is Bad Request/400.
         */
        public AssertResponseEntity statusCodeIsBadRequest() {
            return isNotNull().statusCodeIs(400);
        }

        /**
         * Assert the status code is Method Not Allowed/405.
         */
        public AssertResponseEntity statusCodeIsMethodNotAllowed() {
            return isNotNull().statusCodeIs(405);
        }

        /**
         * Assert the response has a body.
         */
        public AssertResponseEntity hasBody() {
            isNotNull();
            assertThat(actual.hasBody()).isTrue();
            return this;
        }

    }
}

