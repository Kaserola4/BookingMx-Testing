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

