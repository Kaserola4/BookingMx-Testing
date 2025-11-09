/**
 * @fileoverview Graph data structures and algorithms for city proximity calculations.
 * 
 * This module provides an undirected weighted graph implementation for
 * representing cities and distances. It includes validation, construction,
 * and query functionality for finding nearby cities within a specified distance.
 * 
 * All functions are pure and designed for easy unit testing.
 * 
 * @module graph
 * @author BookingMx Team
 * @version 1.0.0
 */

/**
 * Undirected weighted graph for representing cities and distances.
 * 
 * <p>This class uses an adjacency list representation where each city
 * maps to an array of connected cities with their distances.</p>
 * 
 * @class Graph
 * 
 * @example
 * const g = new Graph();
 * g.addCity("Guadalajara");
 * g.addCity("Zapopan");
 * g.addEdge("Guadalajara", "Zapopan", 12);
 * const neighbors = g.neighbors("Guadalajara");
 * // neighbors: [{ to: "Zapopan", distance: 12 }]
 */
export class Graph {
  /**
   * Creates a new empty graph.
   * 
   * @constructor
   */
  constructor() {
    /**
     * Adjacency list mapping city names to arrays of connected cities.
     * @type {Map<string, Array<{to: string, distance: number}>>}
     * @private
     */
    this.adj = new Map();
  }

  /**
   * Adds a city to the graph.
   * 
   * <p>If the city already exists, this operation has no effect.
   * City names must be non-empty strings.</p>
   * 
   * @param {string} name - The name of the city to add.
   * @throws {Error} If the name is invalid (null, undefined, or not a string).
   * 
   * @example
   * const g = new Graph();
   * g.addCity("Guadalajara");
   * g.addCity("Zapopan");
   */
  addCity(name) {
    if (!name || typeof name !== "string") {
      throw new Error("Invalid city name");
    }
    if (!this.adj.has(name)) {
      this.adj.set(name, []);
    }
  }

  /**
   * Adds an undirected edge between two cities.
   * 
   * <p>Both cities must already exist in the graph. The edge is added
   * in both directions since the graph is undirected.</p>
   * 
   * @param {string} from - The first city.
   * @param {string} to - The second city.
   * @param {number} distanceKm - The distance between cities in kilometers.
   * @throws {Error} If either city doesn't exist in the graph.
   * @throws {Error} If the distance is not a finite positive number.
   * 
   * @example
   * const g = new Graph();
   * g.addCity("Guadalajara");
   * g.addCity("Zapopan");
   * g.addEdge("Guadalajara", "Zapopan", 12);
   */
  addEdge(from, to, distanceKm) {
    if (!this.adj.has(from) || !this.adj.has(to)) {
      throw new Error("Unknown city");
    }
    if (!Number.isFinite(distanceKm) || distanceKm < 0) {
      throw new Error("Invalid distance");
    }
    this.adj.get(from).push({ to, distance: distanceKm });
    this.adj.get(to).push({ to: from, distance: distanceKm });
  }

  /**
   * Gets all cities directly connected to the specified city.
   * 
   * @param {string} city - The city to get neighbors for.
   * @returns {Array<{to: string, distance: number}>} Array of neighbor objects.
   * @throws {Error} If the city doesn't exist in the graph.
   * 
   * @example
   * const neighbors = g.neighbors("Guadalajara");
   * neighbors.forEach(n => {
   *   console.log(`${n.to} is ${n.distance} km away`);
   * });
   */
  neighbors(city) {
    if (!this.adj.has(city)) {
      throw new Error("Unknown city");
    }
    return [...this.adj.get(city)];
  }
}

/**
 * Validates graph input data structure.
 * 
 * <p>Checks that the input contains valid cities and edges arrays,
 * with no duplicates and consistent references.</p>
 * 
 * @function validateGraphData
 * @param {Object} data - The graph data to validate.
 * @param {Array<string>} data.cities - Array of city names.
 * @param {Array<{from: string, to: string, distance: number}>} data.edges - Array of edge objects.
 * @returns {{ok: boolean, reason?: string}} Validation result with optional error reason.
 * 
 * @example
 * const result = validateGraphData({
 *   cities: ["A", "B"],
 *   edges: [{ from: "A", to: "B", distance: 10 }]
 * });
 * if (!result.ok) {
 *   console.error("Validation failed:", result.reason);
 * }
 */
export function validateGraphData({ cities, edges }) {
  if (!Array.isArray(cities) || !Array.isArray(edges)) {
    return { ok: false, reason: "cities/edges must be arrays" };
  }
  
  const citySet = new Set(cities);
  if (citySet.size !== cities.length) {
    return { ok: false, reason: "duplicate cities" };
  }
  
  for (const c of cities) {
    if (typeof c !== "string" || !c.trim()) {
      return { ok: false, reason: "invalid city entry" };
    }
  }
  
  for (const e of edges) {
    const { from, to, distance } = e ?? {};
    if (!citySet.has(from) || !citySet.has(to)) {
      return { ok: false, reason: "edge references unknown city" };
    }
    if (!Number.isFinite(distance) || distance < 0) {
      return { ok: false, reason: "invalid distance" };
    }
  }
  
  return { ok: true };
}

/**
 * Constructs a graph from validated city and edge data.
 * 
 * @function buildGraph
 * @param {Array<string>} cities - Array of city names.
 * @param {Array<{from: string, to: string, distance: number}>} edges - Array of edges.
 * @returns {Graph} A fully constructed graph instance.
 * 
 * @example
 * const cities = ["Guadalajara", "Zapopan", "Tlaquepaque"];
 * const edges = [
 *   { from: "Guadalajara", to: "Zapopan", distance: 12 },
 *   { from: "Guadalajara", to: "Tlaquepaque", distance: 10 }
 * ];
 * const graph = buildGraph(cities, edges);
 */
export function buildGraph(cities, edges) {
  const g = new Graph();
  for (const c of cities) {
    g.addCity(c);
  }
  for (const { from, to, distance } of edges) {
    g.addEdge(from, to, distance);
  }
  return g;
}

/**
 * Finds cities near the destination within the specified distance.
 * 
 * <p><strong>Note:</strong> This MVP implementation only considers
 * direct connections (neighbors). Future versions may implement
 * shortest-path algorithms for multi-hop queries.</p>
 * 
 * @function getNearbyCities
 * @param {Graph} graph - The graph to query.
 * @param {string} destination - The destination city to search from.
 * @param {number} [maxDistanceKm=250] - Maximum distance in kilometers.
 * @returns {Array<{city: string, distance: number}>} Sorted array of nearby cities.
 * @throws {Error} If graph is not a Graph instance.
 * 
 * @example
 * const nearby = getNearbyCities(graph, "Guadalajara", 50);
 * console.log("Cities within 50 km:");
 * nearby.forEach(c => {
 *   console.log(`- ${c.city}: ${c.distance} km`);
 * });
 */
export function getNearbyCities(graph, destination, maxDistanceKm = 250) {
  if (!(graph instanceof Graph)) {
    throw new Error("graph must be Graph");
  }
  
  if (typeof destination !== "string" || !graph.adj.has(destination)) {
    return [];
  }
  
  const neighbors = graph.neighbors(destination);
  return neighbors
    .filter(n => n.distance <= maxDistanceKm)
    .sort((a, b) => a.distance - b.distance)
    .map(n => ({ city: n.to, distance: n.distance }));
}

/**
 * Sample dataset for demonstration and testing.
 * 
 * <p>Contains cities in the Jalisco region of Mexico with
 * approximate distances in kilometers.</p>
 * 
 * @constant {Object}
 * @property {Array<string>} cities - List of city names.
 * @property {Array<{from: string, to: string, distance: number}>} edges - City connections.
 * 
 * @example
 * const validation = validateGraphData(sampleData);
 * if (validation.ok) {
 *   const graph = buildGraph(sampleData.cities, sampleData.edges);
 *   const nearby = getNearbyCities(graph, "Guadalajara", 100);
 * }
 */
export const sampleData = {
  cities: [
    "Guadalajara", 
    "Tlaquepaque", 
    "Zapopan", 
    "Tepatitlán", 
    "Lagos de Moreno", 
    "Tala", 
    "Tequila"
  ],
  edges: [
    { from: "Guadalajara", to: "Zapopan", distance: 12 },
    { from: "Guadalajara", to: "Tlaquepaque", distance: 10 },
    { from: "Guadalajara", to: "Tepatitlán", distance: 78 },
    { from: "Guadalajara", to: "Tequila", distance: 60 },
    { from: "Zapopan", to: "Tala", distance: 35 },
    { from: "Tepatitlán", to: "Lagos de Moreno", distance: 85 }
  ]
};