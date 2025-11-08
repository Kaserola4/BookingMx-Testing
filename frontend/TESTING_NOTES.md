# Technical Notes

This document reflects the main technical difficulties encountered during development and testing, as well as the strategies used to overcome them.  
It aims to promote self-reflection, documentation of technical learning, and continuous improvement.

---

## 1. Handling ES Modules in Jest

### Difficulty
When running Jest tests, the following error appeared:

```bash
SyntaxError: Unexpected token 'export'
```

This happened because the project used **ES modules** (`"type": "module"`) while Jest expected **CommonJS** syntax by default.

### Strategy
Configured Jest to support ES modules by:
- Using `"type": "module"` in `package.json`.
- Running tests with:
  ```bash
  node --experimental-vm-modules node_modules/jest/bin/jest.js
  ```
- Configuring `jest.config.js` with empty `transform: {}`

## 2. Mocking the fetch API in Node Environment
### Difficulty

The tests for api.js failed with:

```bash 
TypeError: fetch.mockResolvedValueOnce is not a function
```

Because `fetch` is not available in Node.js by default ad jest does not mock it automatically.

### Strategy
- Created a mock for fetch globally:
```js
beforeAll(() => {
  global.fetch = jest.fn();
});
```