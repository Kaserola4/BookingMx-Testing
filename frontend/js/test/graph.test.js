import {
  Graph,
  validateGraphData,
  buildGraph,
  getNearbyCities,
  sampleData
} from "../graph.js";

describe("Graph class", () => {
  test("should initialize with empty adjacency map", () => {
    const g = new Graph();
    expect(g.adj).toBeInstanceOf(Map);
    expect(g.adj.size).toBe(0);
  });

  test("addCity adds a valid city", () => {
    const g = new Graph();
    g.addCity("A");
    expect(g.adj.has("A")).toBe(true);
    expect(g.adj.get("A")).toEqual([]);
  });

  test("addCity throws for invalid name", () => {
    const g = new Graph();
    expect(() => g.addCity("")).toThrow("Invalid city name");
    expect(() => g.addCity(null)).toThrow("Invalid city name");
  });

  test("addEdge connects two existing cities", () => {
    const g = new Graph();
    g.addCity("A");
    g.addCity("B");
    g.addEdge("A", "B", 10);

    expect(g.adj.get("A")).toEqual([{ to: "B", distance: 10 }]);
    expect(g.adj.get("B")).toEqual([{ to: "A", distance: 10 }]);
  });

  test("addEdge throws for unknown cities or invalid distance", () => {
    const g = new Graph();
    g.addCity("A");

    expect(() => g.addEdge("A", "B", 10)).toThrow("Unknown city");
    g.addCity("B");
    expect(() => g.addEdge("A", "B", -1)).toThrow("Invalid distance");
  });

  test("neighbors returns connected cities", () => {
    const g = new Graph();
    g.addCity("A");
    g.addCity("B");
    g.addEdge("A", "B", 5);

    expect(g.neighbors("A")).toEqual([{ to: "B", distance: 5 }]);
  });

  test("neighbors throws for unknown city", () => {
    const g = new Graph();
    expect(() => g.neighbors("X")).toThrow("Unknown city");
  });
});

describe("validateGraphData", () => {
  test("returns ok for valid data", () => {
    const valid = {
      cities: ["A", "B"],
      edges: [{ from: "A", to: "B", distance: 10 }]
    };
    expect(validateGraphData(valid)).toEqual({ ok: true });
  });

  test("fails if cities or edges not arrays", () => {
    expect(validateGraphData({ cities: {}, edges: [] }).ok).toBe(false);
    expect(validateGraphData({ cities: [], edges: null }).ok).toBe(false);
  });

  test("fails on duplicate cities", () => {
    const data = { cities: ["A", "A"], edges: [] };
    expect(validateGraphData(data)).toEqual({ ok: false, reason: "duplicate cities" });
  });

  test("fails on invalid city entry", () => {
    const data = { cities: ["A", ""], edges: [] };
    expect(validateGraphData(data)).toEqual({ ok: false, reason: "invalid city entry" });
  });

  test("fails on unknown edge cities or invalid distance", () => {
    const data1 = { cities: ["A"], edges: [{ from: "A", to: "B", distance: 5 }] };
    expect(validateGraphData(data1)).toEqual({ ok: false, reason: "edge references unknown city" });

    const data2 = { cities: ["A", "B"], edges: [{ from: "A", to: "B", distance: -1 }] };
    expect(validateGraphData(data2)).toEqual({ ok: false, reason: "invalid distance" });
  });
});

describe("buildGraph", () => {
  test("constructs graph correctly", () => {
    const cities = ["A", "B"];
    const edges = [{ from: "A", to: "B", distance: 5 }];
    const g = buildGraph(cities, edges);

    expect(g).toBeInstanceOf(Graph);
    expect(g.adj.has("A")).toBe(true);
    expect(g.adj.has("B")).toBe(true);
    expect(g.neighbors("A")).toEqual([{ to: "B", distance: 5 }]);
  });
});

describe("getNearbyCities", () => {
  test("returns nearby cities within max distance", () => {
    const g = new Graph();
    g.addCity("A");
    g.addCity("B");
    g.addCity("C");
    g.addEdge("A", "B", 50);
    g.addEdge("A", "C", 300);

    const result = getNearbyCities(g, "A", 200);
    expect(result).toEqual([{ city: "B", distance: 50 }]);
  });

  test("returns empty for invalid graph or city", () => {
    const g = new Graph();
    g.addCity("A");
    expect(() => getNearbyCities({}, "A")).toThrow("graph must be Graph");
    expect(getNearbyCities(g, "Unknown")).toEqual([]);
  });

  test("returns sorted nearby cities", () => {
    const g = new Graph();
    g.addCity("A");
    g.addCity("B");
    g.addCity("C");
    g.addEdge("A", "B", 150);
    g.addEdge("A", "C", 100);

    const result = getNearbyCities(g, "A", 200);
    expect(result).toEqual([
      { city: "C", distance: 100 },
      { city: "B", distance: 150 }
    ]);
  });
});

describe("sampleData", () => {
  test("contains valid structure", () => {
    expect(Array.isArray(sampleData.cities)).toBe(true);
    expect(Array.isArray(sampleData.edges)).toBe(true);
    expect(sampleData.edges[0]).toHaveProperty("from");
    expect(sampleData.edges[0]).toHaveProperty("to");
    expect(sampleData.edges[0]).toHaveProperty("distance");
  });
});