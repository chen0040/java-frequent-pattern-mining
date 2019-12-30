# java-frequent-pattern-mining
Package provides java implementation of frequent pattern mining algorithms such as apriori, fp-growth

[![Build Status](https://travis-ci.org/chen0040/java-frequent-pattern-mining.svg?branch=master)](https://travis-ci.org/chen0040/java-frequent-pattern-mining) [![Coverage Status](https://coveralls.io/repos/github/chen0040/java-frequent-pattern-mining/badge.svg?branch=master)](https://coveralls.io/github/chen0040/java-frequent-pattern-mining?branch=master) 


# Features

* Apriori
* FP-Growth

# Install

Add the following dependency to your POM file:

```xml
<dependency>
  <groupId>com.github.chen0040</groupId>
  <artifactId>java-frequent-pattern-mining</artifactId>
  <version>1.0.1</version>
</dependency>
```

# Usage

### FP Growth

The sample code below shows how to use FPGrowth to obtain the frequent item sets from a list of transactions

```java
 List<List<String>> database = new ArrayList<>();

// each line below add a new transaction to the database
database.add(Arrays.asList("f", "a", "c", "d", "g", "i", "m", "p"));
database.add(Arrays.asList("a", "b", "c", "f", "l", "m", "o"));
database.add(Arrays.asList("b", "f", "h", "j", "o", "w"));
database.add(Arrays.asList("b", "c", "k", "s", "p"));
database.add(Arrays.asList("a", "f", "c", "e", "l", "p", "m", "n"));

AssocRuleMiner method = new FPGrowth();
method.setMinSupportLevel(2);

MetaData metaData = new MetaData(database);

// obtain all frequent item sets with support level not below 2
ItemSets frequent_item_sets = method.minePatterns(database, metaData.getUniqueItems());
frequent_item_sets.stream().forEach(itemSet -> System.out.println("item-set: " + itemSet));

// obtain the max frequent item sets
ItemSets max_frequent_item_sets = method.findMaxPatterns(database, metaData.getUniqueItems());
```

### Apriori

The sample code below shows how to use Apriori to obtain the frequent item sets from a list of transactions

```java
List<List<String>> database = new ArrayList<>();

// each line below add a new transaction to the database
database.add(Arrays.asList("f", "a", "c", "d", "g", "i", "m", "p"));
database.add(Arrays.asList("a", "b", "c", "f", "l", "m", "o"));
database.add(Arrays.asList("b", "f", "h", "j", "o", "w"));
database.add(Arrays.asList("b", "c", "k", "s", "p"));
database.add(Arrays.asList("a", "f", "c", "e", "l", "p", "m", "n"));

AssocRuleMiner method = new Apriori();
method.setMinSupportLevel(2);

MetaData metaData = new MetaData(database);

// obtain all frequent item sets with support level not below 2
ItemSets frequent_item_sets = method.minePatterns(database, metaData.getUniqueItems());
frequent_item_sets.stream().forEach(itemSet -> System.out.println("item-set: " + itemSet));

// obtain the max frequent item sets
ItemSets max_frequent_item_sets = method.findMaxPatterns(database, metaData.getUniqueItems());
```

