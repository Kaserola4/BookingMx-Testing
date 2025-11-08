import {
  listReservations,
  createReservation,
  updateReservation,
  cancelReservation
} from "../api.js";

import { jest } from "@jest/globals";

beforeAll(() => {
  global.fetch = jest.fn();
});

beforeEach(() => {
  jest.clearAllMocks();
});

describe("api.js", () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe("listReservations", () => {
    test("should return reservations on success", async () => {
      fetch.mockResolvedValueOnce({
        ok: true,
        json: () => Promise.resolve([{ id: 1 }])
      });

      const result = await listReservations();

      expect(result).toEqual([{ id: 1 }]);
      expect(fetch).toHaveBeenCalledWith("http://localhost:8080/api/reservations");
    });

    test("should throw an error on failure", async () => {
      fetch.mockResolvedValueOnce({ ok: false });
      await expect(listReservations()).rejects.toThrow("Failed to fetch reservations");
    });
  });

  describe("createReservation", () => {
    const payload = { guest: "John" };

    test("should create a reservation successfully", async () => {
      fetch.mockResolvedValueOnce({
        ok: true,
        json: () => Promise.resolve({ id: 1 })
      });

      const result = await createReservation(payload);

      expect(result.id).toBe(1);
      expect(fetch).toHaveBeenCalledWith(
        "http://localhost:8080/api/reservations",
        expect.objectContaining({
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload)
        })
      );
    });

    test("should throw error with message when server returns message", async () => {
      fetch.mockResolvedValueOnce({
        ok: false,
        json: () => Promise.resolve({ message: "Error message" })
      });

      await expect(createReservation({})).rejects.toThrow("Error message");
    });

    test("should throw generic error when no message returned", async () => {
      fetch.mockResolvedValueOnce({
        ok: false,
        json: () => Promise.resolve({})
      });

      await expect(createReservation({})).rejects.toThrow("Create failed");
    });
  });

  describe("updateReservation", () => {
    test("should update a reservation successfully", async () => {
      fetch.mockResolvedValueOnce({
        ok: true,
        json: () => Promise.resolve({ updated: true })
      });

      const result = await updateReservation(5, { a: 1 });

      expect(result.updated).toBe(true);
      expect(fetch).toHaveBeenCalledWith(
        "http://localhost:8080/api/reservations/5",
        expect.objectContaining({ method: "PUT" })
      );
    });

    test("should throw error with message when server returns message", async () => {
      fetch.mockResolvedValueOnce({
        ok: false,
        json: () => Promise.resolve({ message: "Bad update" })
      });

      await expect(updateReservation("5", {})).rejects.toThrow("Bad update");
    });
  });

  describe("cancelReservation", () => {
    test("should cancel a reservation successfully", async () => {
      fetch.mockResolvedValueOnce({
        ok: true,
        json: () => Promise.resolve({ deleted: true })
      });

      const result = await cancelReservation(9);
      expect(result.deleted).toBe(true);
    });

    test("should throw error with message when server returns message", async () => {
      fetch.mockResolvedValueOnce({
        ok: false,
        json: () => Promise.resolve({ message: "Cannot cancel" })
      });

      await expect(cancelReservation("x")).rejects.toThrow("Cannot cancel");
    });

    test("should throw generic error when no message returned", async () => {
      fetch.mockResolvedValueOnce({
        ok: false,
        json: () => Promise.resolve({})
      });

      await expect(cancelReservation("x")).rejects.toThrow("Cancel failed");
    });
  });
});
