package com.nextmatch.vero.jsonapiadapter;

/**
 * @author vero
 * @since 2016. 11. 28.
 */
interface JsonApiStrings {

    String simpleSingleResource = "{\n" +
            "  \"data\": {\n" +
            "    \"type\": \"articles\",\n" +
            "    \"id\": \"1\",\n" +
            "    \"attributes\": {\n" +
            "      \"title\": \"JSON API paints my bikeshed!\",\n" +
            "      \"body\": \"The shortest article. Ever.\",\n" +
            "      \"created\": \"2015-05-22T14:56:29.000Z\",\n" +
            "      \"updated\": \"2015-05-22T14:56:28.000Z\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    String simpleArrayResource = "{\n" +
            "  \"data\": [{\n" +
            "    \"type\": \"articles\",\n" +
            "    \"id\": \"1\",\n" +
            "    \"attributes\": {\n" +
            "      \"title\": \"JSON API paints my bikeshed!\",\n" +
            "      \"body\": \"The shortest article. Ever.\",\n" +
            "      \"created\": \"2015-05-22T14:56:29.000Z\",\n" +
            "      \"updated\": \"2015-05-22T14:56:28.000Z\"\n" +
            "    }\n" +
            "  }]\n" +
            "}";

    String simpleArrayRelationshipsResource = "{\n" +
            "  \"data\": [{\n" +
            "    \"type\": \"articles\",\n" +
            "    \"id\": \"1\",\n" +
            "    \"attributes\": {\n" +
            "      \"title\": \"JSON API paints my bikeshed!\"\n" +
            "    },\n" +
            "    \"relationships\": {\n" +
            "      \"author\": {\n" +
            "        \"links\": {\n" +
            "          \"self\": \"http://example.com/articles/1/relationships/author\",\n" +
            "          \"related\": \"http://example.com/articles/1/author\"\n" +
            "        },\n" +
            "        \"data\": { \"type\": \"people\", \"id\": \"9\" }\n" +
            "      },\n" +
            "      \"comments\": {\n" +
            "        \"links\": {\n" +
            "          \"self\": \"http://example.com/articles/1/relationships/comments\",\n" +
            "          \"related\": \"http://example.com/articles/1/comments\"\n" +
            "        },\n" +
            "        \"data\": [\n" +
            "          { \"type\": \"comments\", \"id\": \"5\" },\n" +
            "          { \"type\": \"comments\", \"id\": \"12\" }\n" +
            "        ]\n" +
            "      }\n" +
            "    },\n" +
            "    \"links\": {\n" +
            "      \"self\": \"http://example.com/articles/1\"\n" +
            "    }\n" +
            "  }]\n" +
            "}";

    String error = "{\n" +
            "  \"errors\": [\n" +
            "    {\n" +
            "      \"status\": \"422\",\n" +
            "      \"source\": { \"pointer\": \"/data/attributes/first-name\" },\n" +
            "      \"title\":  \"Invalid Attribute\",\n" +
            "      \"detail\": \"First name must contain at least three characters.\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    String included = "{\n" +
            "  \"links\": {\n" +
            "    \"self\": \"http://example.com/articles\",\n" +
            "    \"next\": \"http://example.com/articles?page[offset]=2\",\n" +
            "    \"last\": \"http://example.com/articles?page[offset]=10\"\n" +
            "  },\n" +
            "  \"data\": [{\n" +
            "    \"type\": \"articles\",\n" +
            "    \"id\": \"1\",\n" +
            "    \"attributes\": {\n" +
            "      \"title\": \"JSON API paints my bikeshed!\"\n" +
            "    },\n" +
            "    \"relationships\": {\n" +
            "      \"author\": {\n" +
            "        \"links\": {\n" +
            "          \"self\": \"http://example.com/articles/1/relationships/author\",\n" +
            "          \"related\": \"http://example.com/articles/1/author\"\n" +
            "        },\n" +
            "        \"data\": { \"type\": \"people\", \"id\": \"9\" }\n" +
            "      },\n" +
            "      \"comments\": {\n" +
            "        \"links\": {\n" +
            "          \"self\": \"http://example.com/articles/1/relationships/comments\",\n" +
            "          \"related\": \"http://example.com/articles/1/comments\"\n" +
            "        },\n" +
            "        \"data\": [\n" +
            "          { \"type\": \"comments\", \"id\": \"5\" },\n" +
            "          { \"type\": \"comments\", \"id\": \"12\" }\n" +
            "        ]\n" +
            "      }\n" +
            "    },\n" +
            "    \"links\": {\n" +
            "      \"self\": \"http://example.com/articles/1\"\n" +
            "    }\n" +
            "  }],\n" +
            "  \"included\": [{\n" +
            "    \"type\": \"people\",\n" +
            "    \"id\": \"9\",\n" +
            "    \"attributes\": {\n" +
            "      \"first-name\": \"Dan\",\n" +
            "      \"last-name\": \"Gebhardt\",\n" +
            "      \"twitter\": \"dgeb\"\n" +
            "    },\n" +
            "    \"links\": {\n" +
            "      \"self\": \"http://example.com/people/9\"\n" +
            "    }\n" +
            "  }, {\n" +
            "    \"type\": \"comments\",\n" +
            "    \"id\": \"5\",\n" +
            "    \"attributes\": {\n" +
            "      \"body\": \"First!\"\n" +
            "    },\n" +
            "    \"relationships\": {\n" +
            "      \"author\": {\n" +
            "        \"data\": { \"type\": \"people\", \"id\": \"2\" }\n" +
            "      }\n" +
            "    },\n" +
            "    \"links\": {\n" +
            "      \"self\": \"http://example.com/comments/5\"\n" +
            "    }\n" +
            "  }, {\n" +
            "    \"type\": \"comments\",\n" +
            "    \"id\": \"12\",\n" +
            "    \"attributes\": {\n" +
            "      \"body\": \"I like XML better\"\n" +
            "    },\n" +
            "    \"relationships\": {\n" +
            "      \"author\": {\n" +
            "        \"data\": { \"type\": \"people\", \"id\": \"9\" }\n" +
            "      }\n" +
            "    },\n" +
            "    \"links\": {\n" +
            "      \"self\": \"http://example.com/comments/12\"\n" +
            "    }\n" +
            "  }]\n" +
            "}";
}
