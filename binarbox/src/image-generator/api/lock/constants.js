//TODO update dimensions
const IMAGE_WITH_TEXT = {
  WIDTH: 420,
  HEIGHT: 300,
};

const IMAGE_WITHOUT_TEXT = {
  WIDTH: 1920,
  HEIGHT: 1080,
};

const TEXT = {
  LINE_HEIGHT: 15,
  LINE_HEIGHT_WITHOUT_IMAGE: 52
};

const MIME_TYPES = {
  PNG: 'image/png',
  JSON: 'application/json',
};

const LINE_END = '{LINE_END}';

const HTTP_HEADERS = {
  CONTENT_TYPE: 'Content-Type',
};

const API_URL = 'https://localhost:6060/api/v0/download/file/{LOCK_ID}';

const FONT = {
  ARIAL: 'Arial',
};

module.exports = {
  MIME_TYPES: MIME_TYPES,
  LINE_END: LINE_END,
  IMAGE_WITH_TEXT: IMAGE_WITH_TEXT,
  IMAGE_WITHOUT_TEXT: IMAGE_WITHOUT_TEXT,
  HTTP_HEADERS: HTTP_HEADERS,
  API_URL: API_URL,
  TEXT: TEXT,
};
