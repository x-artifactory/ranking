<!-- PROJECT LOGO -->
<br/>
<div align="center">
 <hr>
<h3 align="center">RANKING STRATEGY</h3>
  <p align="center">ADVANCED HIERARCHICAL RANKING FRAMEWORK<br/>
 <hr>
</div>

<!-- ABOUT THE PROJECT -->

The Ranking Strategy Framework is a simple, yet flexible and robust tool for addressing hierarchical ranking tasks, such as collection sorting or searching for its "best element", in a unified, standardized way. It offers a minimalistic application programming interface together with a thoughtful design ensuring enhanced ranking process stability.

Its API consists of only two elements that are a collection interface providing ranking operations and an abstract class that is to be extended by a user with definitions of ranking criteria evaluation functions.

Here is how to use it:
```
// initial unordered collection 
final Collection<Response> responses = responseAsyncGenerator();

// instantiation of the RankedCollection with ranking rules supplier 
final var ranker = RankedCollection.from(responses, ResponseRanks::new);

// get sorted collection
final var responsesAsc = ranker.asc();
final var responsesDesc = ranker.desc();

// get minimal and maximal elements
final var responseMin = ranker.min();
final var responseMax = ranker.max();
```

Here is how to define ranking criteria evaluation functions:

```
public class ResponseRanks extends RankWrapper<Response> {

@Override
public List<Supplier<Comparable<?>>> rankFunctionList() {
      return List.of(
          this::positiveRank,
          this::negativeRank,
          this::anotherBigRank,
          this::alphabeticalRank);
      }

     Float positiveRank() {
         return get().score();
     }

     double negativeRank() {
         return -get().score();
     }

     String alphabeticalRank() {
         return get().name();
     }

     BigDecimal anotherBigRank() {
         ...

 }
```
The Ranking Strategy Framework provides two core features addressing the non-deterministic behavior issue. The first one is to throw an exception if all ranking functions are exhausted but elements having all identical rank values remain; the second is to exclude direct use of floating point numbers within sorting algorithms.

Indeed, when the system fails to distinguish two elements based on their calculated ranking criteria values, it usually relies on implicit input information stored in the initial collection sorting, which can vary between runs because of factors beyond our control. Such an assumption, if implemented, would violate the principle of locality, making it impossible to reason about this program component correctness without our needing to examine any other program parts.

The next point is that the use of floating-point type ranking criteria reveals an interesting inconsistency in meeting two critical conditions. Numerically stable comparison of floating-point numbers has to be evaluated with a certain precision tolerance, which in turn makes it possible the violation of the transitivity relation required for numerically stable sorting.

To get around this difficulty, we first create an ordered sequence of disjoint intervals made up from sorted initial floating-point number values in such a way that any interval contains its first value followed by all values, each of which is equal to a value immediately preceding it, given equality is estimated with a certain tolerance; secondly we exclude the direct raw use of floating-point numbers in sorting by translating them into integer type indexes of intervals they are falling into.

### Installation

Incorporate this framework into your project by including Maven dependencies into your pom.xml file:
```
<dependency>
	<groupId>io.github.x-artifactory</groupId>
	<artifactId>ranking-strategy</artifactId>
</dependency>
```


<!-- CONTACT -->
### Contact

Project Link: [https://github.com/x-artifactory/ranking](https://github.com/x-artifactory/ranking)

email: abbbrs@icloud.com 

Write for Alexey Baranov, preferably in French, English, Russian or Portuguese languages.