const chai = require("chai");
const assert = chai.assert;

const Utils = require("../utils");
const utils = new Utils();

const { articles } = require("../output.json");

describe("testing craftObjSearchStr function", () => {
  test("craftObjSearchStr_NullInput_ReturnsNonGovFilter", () => {
    const result = utils.craftQueryObj();
    assert.deepEqual(result, {
      type: {
        $ne: "Gov",
      },
    });
  });

  test("craftObjSearchStr_StrInput_ReturnsProperObjectWithNonGovFilter", () => {
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
