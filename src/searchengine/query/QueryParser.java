package searchengine.query;

public class QueryParser {

    public Query parse(String query) {

        String normalizedQuery =
                query.toLowerCase().trim();

        if (normalizedQuery.isEmpty()) {
            return new Query(
                    QueryType.EMPTY,
                    new String[0]
            );
        }

        // Exact phrase
        if (normalizedQuery.startsWith("\"")
                && normalizedQuery.endsWith("\"")) {

            String phrase =
                    normalizedQuery.substring(
                            1,
                            normalizedQuery.length() - 1
                    ).trim();

            return new Query(
                    QueryType.PHRASE,
                    phrase.split("\\s+")
            );
        }

        // OR query
        if (normalizedQuery.contains(" or ")) {

            String[] terms =
                    normalizedQuery.split(
                            "\\s+or\\s+"
                    );

            return new Query(
                    QueryType.OR,
                    terms
            );
        }

        // AND query
        if (normalizedQuery.contains(" and ")) {

            String[] terms =
                    normalizedQuery.split(
                            "\\s+and\\s+"
                    );

            return new Query(
                    QueryType.AND,
                    terms
            );
        }

        // Normal query
        return new Query(
                QueryType.AND,
                normalizedQuery.split("\\s+")
        );
    }

    public enum QueryType {
        EMPTY,
        AND,
        OR,
        PHRASE
    }

    public static class Query {

        private final QueryType type;
        private final String[] terms;

        public Query(
                QueryType type,
                String[] terms
        ) {
            this.type = type;
            this.terms = terms;
        }

        public QueryType getType() {
            return type;
        }

        public String[] getTerms() {
            return terms;
        }
    }
}