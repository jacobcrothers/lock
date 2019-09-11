//TODO update dimensions
const IMAGE = {
  WIDTH: 400,
  HEIGHT: 300,
};

const TEXT = {
  LINE_HEIGHT: 15
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
  IMAGE: IMAGE,
  HTTP_HEADERS: HTTP_HEADERS,
  API_URL: API_URL,
  TEXT: TEXT
};
