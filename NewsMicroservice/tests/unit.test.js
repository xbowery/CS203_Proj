const chai = require("chai");
const assert = chai.assert;

const Utils = require("../utils");
const utils = new Utils();

const { articles } = require("../output.json");

describe("testing craftQueryObj function", () => {
  test("craftQueryObj_NullInput_ReturnsNonGovFilter", () => {
    const result = utils.craftQueryObj();
    assert.deepEqual(result, {
      type: {
        $ne: "Gov",
      },
    });
  });

  test("craftQueryObj_StrInput_ReturnsProperObjectWithNonGovFilter", () => {
    const result = utils.craftQueryObj("covid");
    assert.deepEqual(result, {
      $or: [
        {
          title: /covid/i,
        },
        {
          content: /covid/i,
        },
      ],
      type: {
        $ne: "Gov",
      },
    });
  });
});

describe("testing craftBulkObject function with null inputs", () => {
  test("craftBulkObject_NullInput_ReturnsEmptyObjectArray", () => {
    const result = utils.craftBulkWriteObject();
    assert.deepEqual(result, [{}]);
  });

  test("craftBulkObject_NullInputNewsObj_ReturnsEmptyObjectArray", () => {
    const result = utils.craftBulkWriteObject(articles);
    assert.deepEqual(result, [{}]);
  });

  test("craftBulkObject_NullInputCategory_ReturnsEmptyObjectArray", () => {
    const result = utils.craftBulkWriteObject([{}], "Regular");
    assert.deepEqual(result, [{}]);
  });
});

describe("craftBulkObjectProper", () => {
  const result = utils.craftBulkWriteObject(articles, "Regular");

  test("CorrectInput_ReturnsProperObjectArray", () => {
    assert.isArray(result);
    assert.equal(result.length, 1);
  });

  const { updateOne } = result[0];

  test("CorrectInput_ReturnsCorrectKeys", () => {
    assert.isNotEmpty(updateOne);
    assert.containsAllKeys(updateOne, [
      "filter",
      "update",
      "upsert",
      "timestamps",
    ]);
    assert.isTrue(updateOne.upsert);
    assert.isTrue(updateOne.timestamps);
  });

  test("CorrectInput_ReturnsCorrectFilterObj", () => {
    assert.deepEqual(updateOne.filter, {
      url: "https://www.channelnewsasia.com/world/australia-melbourne-covid-19-lockdown-ease-2249786",
    });
  });

  test("CorrectInput_ReturnsCorrectSetCommand", () => {
    let expectedObj = [...articles][0];
    expectedObj.source = "CNA";
    expectedObj.imageUrl =
      "https://www.channelnewsasia.com/world/australia-melbourne-covid-19-lockdown-ease-2249786";
    expectedObj.source = "Regular";

    assert.deepEqual(updateOne.update, {
      $set: expectedObj,
    });
  });
});

describe("Test Creating Feed Object for RSS", () => {
  const sampleFeed = {
    title: "The water is too deep, so he improvises",
    link: "https://www.reddit.com/r/funny/comments/3skxqc/the_water_is_too_deep_so_he_improvises/",
    pubDate: "Thu, 12 Nov 2015 21:16:39 +0000",
    content:
      '<table> <tr><td> <a href="https://www.reddit.com/r/funny/comments/3skxqc/the_water_is_too_deep_so_he_improvises/"><img src="https://b.thumbs.redditmedia.com/z4zzFBqZ54WT-rFfKXVor4EraZtJVw7AodDvOZ7kitQ.jpg" alt="The water is too deep, so he improvises" title="The water is too deep, so he improvises" /></a> </td><td> submitted by <a href="https://www.reddit.com/user/cakebeerandmorebeer"> cakebeerandmorebeer </a> to <a href="https://www.reddit.com/r/funny/"> funny</a> <br/> <a href="http://i.imgur.com/U407R75.gifv">[link]</a> <a href="https://www.reddit.com/r/funny/comments/3skxqc/the_water_is_too_deep_so_he_improvises/">[275 comments]</a> </td></tr></table>',
    contentSnippet:
      "submitted by  cakebeerandmorebeer  to  funny \n [link] [275 comments]",
    guid: "https://www.reddit.com/r/funny/comments/3skxqc/the_water_is_too_deep_so_he_improvises/",
    categories: ["funny"],
    isoDate: "2015-11-12T21:16:39.000Z",
  };

  test("craftFeedObject_properInput_ReturnsFeedObject", () => {
    const returnTestObj = utils.craftFeedObject(sampleFeed);
    const { title, url, source, content, imageUrl, type } = returnTestObj;

    assert.equal(title, sampleFeed.title);
    assert.equal(url, sampleFeed.link);
    assert.equal(source, "MOH");
    assert.equal(content, "-");
    assert.equal(imageUrl, "-");
    assert.equal(type, "Gov");
  });
});

describe("Testing create RSS DB Update Object", () => {
  const testFeedObj = {
    title:
      "Melbourne to ease world's longest COVID-19 lockdowns as vaccinations rise",
    url: "https://www.channelnewsasia.com/world/australia-melbourne-covid-19-lockdown-ease-2249786",
    source: "MOH",
    content: "-",
    imageUrl: "-",
    type: "Gov",
  };

  const result = utils.createRssUpdateObj(testFeedObj);
  const { updateOne } = result;
  test("createRssUpdateObj_properInput_ReturnsDBUpdateObj", () => {
    assert.isNotEmpty(updateOne);
    assert.containsAllKeys(updateOne, [
      "filter",
      "update",
      "upsert",
      "timestamps",
    ]);
    assert.isTrue(updateOne.upsert);
    assert.isTrue(updateOne.timestamps);
  });

  test("CorrectInput_ReturnsCorrectFilterObj", () => {
    assert.deepEqual(updateOne.filter, {
      url: "https://www.channelnewsasia.com/world/australia-melbourne-covid-19-lockdown-ease-2249786",
    });
  });
});
