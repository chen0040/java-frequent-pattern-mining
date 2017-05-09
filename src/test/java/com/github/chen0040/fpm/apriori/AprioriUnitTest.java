package com.github.chen0040.fpm.apriori;


import com.github.chen0040.fpm.AssocRuleMiner;
import com.github.chen0040.fpm.data.ItemSets;
import com.github.chen0040.fpm.data.MetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by xschen on 9/5/2017.
 */
public class AprioriUnitTest {

   private static final Logger logger = LoggerFactory.getLogger(AprioriUnitTest.class);

   @Test public void test_minePatterns() throws Exception {

      logger.info("Using Apriori");

      List<List<String>> database = createSimpleData();
      ItemSets fis = basicTest(database);
      fis.stream().forEach(itemSet -> logger.info("item-set: {}", itemSet));



   }

   @Test public void test_mineMaxPatterns() throws Exception{
      logger.info("Using Apriori for max patterns mining");
      List<List<String>> database = createSimpleData();
      ItemSets fis = basicTestMax(database);
      fis.stream().forEach(itemSet -> logger.info("max-item-set: {}", itemSet));
      logger.info("Finding Closed Pattern");
   }

   public ItemSets basicTest(List<List<String>> database){
      AssocRuleMiner method = new Apriori();
      method.setMinSupportLevel(2);

      MetaData metaData = new MetaData(database);
      ItemSets fis = method.minePatterns(database, metaData.getUniqueItems());

      return fis;
   }

   public ItemSets basicTestMax(List<List<String>> database){
      AssocRuleMiner method = new Apriori();
      method.setMinSupportLevel(2);

      MetaData metaData = new MetaData(database);
      return method.findMaxPatterns(database, metaData.getUniqueItems());
   }

   public List<List<String>> createSimpleData(){
      List<List<String>> database = new ArrayList<>();

      database.add(Arrays.asList("f", "a", "c", "d", "g", "i", "m", "p"));
      database.add(Arrays.asList("a", "b", "c", "f", "l", "m", "o"));
      database.add(Arrays.asList("b", "f", "h", "j", "o", "w"));
      database.add(Arrays.asList("b", "c", "k", "s", "p"));
      database.add(Arrays.asList("a", "f", "c", "e", "l", "p", "m", "n"));

      database.forEach(transaction -> logger.info("transaction: {}", transaction.stream().collect(Collectors.joining(", "))));

      return database;
   }

}
