package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.concrete.dictionaries.KVPair;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed. Must be non-null and must not contain
     *                  nulls.
     * @param decay     Represents the "decay" factor when computing page rank (see spec). Must be a
     *                  number between 0 and 1, inclusive.
     * @param epsilon   When the difference in page ranks is less than or equal to this number,
     *                  stop iterating. Must be a non-negative number.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges. Must be a non-negative number. (A limit of 0 should
     *                  simply return the initial page rank values from 'computePageRank'.)
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.

        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        IDictionary<URI, ISet<URI>> pageGraph = new ChainedHashDictionary<>();
        ISet<URI> allUniquePage = new ChainedHashSet<>();

        for (Webpage page : webpages) {
            allUniquePage.add(page.getUri());
        }

        for (Webpage page : webpages) {
            IList<URI> pageLinks = page.getLinks();
            ISet<URI> linkPages = new ChainedHashSet<>();
            for (URI link : pageLinks) {
                if (allUniquePage.contains(link)) {
                    linkPages.add(link);
                }
            }
            if (linkPages.contains(page.getUri())) {
                linkPages.remove(page.getUri());
            }
            pageGraph.put(page.getUri(), linkPages);
        }

        return pageGraph;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less than or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
        // Step 1: The initialize step should go here
        IDictionary<URI, Double> oldPageRank = new ChainedHashDictionary<>();

        for (KVPair<URI, ISet<URI>> pair : graph) {
            oldPageRank.put(pair.getKey(), 1.0 / graph.size());
        }

        for (int i = 0; i < limit; i++) {
            // Step 2: The update step should go here
            IDictionary<URI, Double> newPageRank = new ChainedHashDictionary<>();

            for (KVPair<URI, ISet<URI>> pageLinks : graph) {
                if (pageLinks.getValue().isEmpty()) {
                    double newScore = decay * oldPageRank.get(pageLinks.getKey()) / graph.size();
                    for (KVPair<URI, Double> pageValue : oldPageRank) {
                        newPageRank.put(pageValue.getKey(),
                                newPageRank.getOrDefault(pageValue.getKey(), 0.0) + newScore);
                    }
                }
                else {
                    ISet<URI> links = pageLinks.getValue();
                    double oldScore = oldPageRank.get(pageLinks.getKey());
                    for (URI link : links) {
                        newPageRank.put(link,
                                newPageRank.getOrDefault(link, 0.0) + decay * oldScore / links.size());
                    }
                }

                newPageRank.put(pageLinks.getKey(),
                        newPageRank.getOrDefault(pageLinks.getKey(), 0.0) + (1 - decay) / graph.size());
            }

            // Step 3: the convergence step should go here.
            // Return early if we've converged.
            boolean converge = true;

            for (KVPair<URI, Double> pageValue : newPageRank) {
                if (Math.abs(oldPageRank.get(pageValue.getKey()) - newPageRank.get(pageValue.getKey())) >= epsilon) {
                    converge = false;
                }
            }

            if (converge) {
                return newPageRank;
            }
            else {
                oldPageRank = newPageRank;
            }
        }
        return oldPageRank;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        return pageRanks.get(pageUri);
    }
}
