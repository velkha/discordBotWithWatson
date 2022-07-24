package sparkles.spelling_corrector;

import java.util.*;
import java.util.stream.Stream;

/**
 * Corrector por proximidad a la palabra
 */
public class SpellingCorrector implements ISpellingCorrector {


    private Map<String, Integer> dictionary = null;

    public SpellingCorrector(int lruCount) {
        this.dictionary = Collections.synchronizedMap(new LruCache<>(lruCount));
    }

    @Override
    public void putWord(String word) {
        word = word.toLowerCase();
        if (dictionary.containsKey(word)) {
            dictionary.put(word, (dictionary.get(word) + 1));
        }
        else {
            dictionary.put(word, 1);
        }
    }

    @Override
    public String correct(String word) {
        if (word == null || word.trim().isEmpty()) {
            return word;
        }

        word = word.toLowerCase();

        // Si la palabra existe no hacemos nada
        if (dictionary.containsKey(word)) {
            return word;
        }

        Map<String, Integer> possibleMatches = new HashMap<>();

        List<String> closeEdits = wordEdits(word);
        for (String closeEdit: closeEdits) {
            if (dictionary.containsKey(closeEdit)) {
                possibleMatches.put(closeEdit, this.dictionary.get(closeEdit));
            }
        }

        if (!possibleMatches.isEmpty()) {
            // Menos posibles primero
            Object[] matches = this.sortByValue(possibleMatches).keySet().toArray();

            // Buscamos por mismo tamaño de palabra
            String bestMatch = "";
            for(Object o: matches) {
                if (o.toString().length() == word.length()) {
                    bestMatch = o.toString();
                }
            }

            if (!bestMatch.trim().isEmpty()) {
                return bestMatch;
            }

            // Devolvemos el mejor resultado
            return matches[matches.length - 1].toString();
        }

        // No hemos encontrado nada por lo que procedemos a realizar la busqueda con el wordedits
        // y mostramos los resultados posibles
        List<String> furtherEdits = new ArrayList<>();
        for(String closeEdit: closeEdits) {
            furtherEdits.addAll(this.wordEdits(closeEdit));
        }

        for (String futherEdit: furtherEdits) {
            if (dictionary.containsKey(futherEdit)) {
                possibleMatches.put(futherEdit, this.dictionary.get(futherEdit));
            }
        }

        if (!possibleMatches.isEmpty()) {
            // Ordenamos los posible matches
            Object[] matches = this.sortByValue(possibleMatches).keySet().toArray();

            // Trata de conseguir primero algo con el mismo tamanio de palabra
            String bestMatch = "";
            for(Object o: matches) {
                if (o.toString().length() == word.length()) {
                    bestMatch = o.toString();
                }
            }

            if (!bestMatch.trim().isEmpty()) {
                return bestMatch;
            }

            // Devuelve el mejor resultado
            return matches[matches.length - 1].toString();
        }


        // If unable to find something better return the same string
        return word;
    }

    @Override
    public boolean containsWord(String word) {
        if (dictionary.containsKey(word)) {
            return true;
        }

        return false;
    }


    /**
     * Suponemos que la primera letra de la palabra es correcta para devolver las posibles palabras correctas
     */
    public List<String> wordEdits(String word) {
        List<String> closeWords = new ArrayList<String>();

        for (int i = 1; i < word.length() + 1; i++) {
            for (char character = 'a'; character <= 'z'; character++) {
                // Maybe they forgot to type a letter? Try adding one
                StringBuilder sb = new StringBuilder(word);
                sb.insert(i, character);
                closeWords.add(sb.toString());
            }
        }

        for (int i = 1; i < word.length(); i++) {
            for (char character = 'a'; character <= 'z'; character++) {
                // Maybe they mistyped a single letter? Try replacing them all
                StringBuilder sb = new StringBuilder(word);
                sb.setCharAt(i, character);
                closeWords.add(sb.toString());

                // Maybe they added an extra letter? Try deleting one
                sb = new StringBuilder(word);
                sb.deleteCharAt(i);
                closeWords.add(sb.toString());
            }
        }

        return closeWords;
    }


    /**
     * Ordenamos segun el valor que recogemos del map
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();

        st.sorted( Map.Entry.comparingByValue() ).forEachOrdered( e -> result.put(e.getKey(), e.getValue()) );

        return result;
    }
    public class LruCache<A, B> extends LinkedHashMap<A, B> {
        private final int maxEntries;

        public LruCache(final int maxEntries) {
            super(maxEntries + 1, 1.0f, true);
            this.maxEntries = maxEntries;
        }

        @Override
        protected boolean removeEldestEntry(final Map.Entry<A, B> eldest) {
            return super.size() > maxEntries;
        }
    }

}