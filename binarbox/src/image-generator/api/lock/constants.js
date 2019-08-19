//TODO update dimensions
const IMAGE_BASE_DIMENSIONS = {
  WIDTH: 1920,
  HEIGHT: 1382,
};

const MIME_TYPES = {
  PNG: 'image/png',
  JSON: 'application/json',
};

const LINE_END = '{LINE_END}';

const HTTP_HEADERS = {
    CONTENT_TYPE: "Content-Type"
};

const API_URL = "https://localhost:6060/api/v0/download/file/{LOCK_ID}";


const FONT = {
    ARIAL: "Arial"
};

module.exports = {
  MIME_TYPES: MIME_TYPES,
  LINE_END: LINE_END,
  IMAGE_BASE_DIMENSIONS: IMAGE_BASE_DIMENSIONS,
  HTTP_HEADERS: HTTP_HEADERS,
  API_URL: API_URL
};
