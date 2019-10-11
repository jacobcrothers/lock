const { createCanvas, loadImage, registerFont } = require('canvas');
const Joi = require('joi');
const {
  MIME_TYPES,
  IMAGE_WITH_TEXT,
  IMAGE_WITHOUT_TEXT,
  LINE_END,
  HTTP_HEADERS,
  API_URL,
  TEXT,
} = require('./constants');

// registerFont(
//   '/Users/razvan/Work/Lockbridges/lock/binarbox/src/image-generator/assets/fonts/roboto/Roboto-Regular.ttf',
//   {
//     family: 'Roboto',
//   },
// );

const setResponseStatus = (ctx, status, message) => {
  ctx.status = status;

  let errorResponseBody = {
    status: 'ERROR',
    errorCode: 'IG-0',
    message: message || 'Generic error',
  };

  switch (status) {
    case 200:
      ctx.set(HTTP_HEADERS.CONTENT_TYPE, MIME_TYPES.PNG);
      return;
    case 500:
      ctx.set(HTTP_HEADERS.CONTENT_TYPE, MIME_TYPES.JSON);
      errorResponseBody = { ...errorResponseBody, errorCode: 'IG-1' };
      break;
    case 400:
      ctx.set(HTTP_HEADERS.CONTENT_TYPE, MIME_TYPES.JSON);
      errorResponseBody = { ...errorResponseBody, errorCode: 'IG-2' };
      break;
    default:
      ctx.set(HTTP_HEADERS.CONTENT_TYPE, MIME_TYPES.JSON);
      ctx.body = JSON.stringify(errorResponseBody);
  }
  ctx.body = JSON.stringify(errorResponseBody);
};

const generateImage = async ctx => {
  const { templateId } = ctx.query;
  const imageURL = API_URL.replace('{LOCK_ID}', ctx.query.templateId);
  await loadImage(imageURL, {})
    .then(image => {
      const canvas = createCanvas(IMAGE_WITH_TEXT.WIDTH, IMAGE_WITH_TEXT.HEIGHT);
      const canvasContext = canvas.getContext('2d');
      const { font, fontSize, message, color } = ctx.request.query;
      const [firstLine, secondLine] = message.split(LINE_END);

      //TODO Validate font and register new fonts

      canvasContext.font = `${fontSize}px ${font}`;
      canvasContext.fillStyle = color;
      canvasContext.drawImage(image, 0, 0, IMAGE_WITH_TEXT.WIDTH, IMAGE_WITH_TEXT.HEIGHT);
      canvasContext.textAlign = 'center';

      const firstLineHeight = IMAGE_WITH_TEXT.HEIGHT / 2 + TEXT.LINE_HEIGHT;
      const secondLineHeight = IMAGE_WITH_TEXT.HEIGHT / 2 + 2 * TEXT.LINE_HEIGHT;

      canvasContext.fillText(firstLine, IMAGE_WITH_TEXT.WIDTH / 2, firstLineHeight);
      canvasContext.fillText(secondLine, IMAGE_WITH_TEXT.WIDTH / 2, secondLineHeight);
      ctx.body = canvas.toBuffer(MIME_TYPES.PNG, {});
      setResponseStatus(ctx, 200);
    })
    .catch(e => {
      ctx.log.error(
        `Unable to load image with id ${templateId} with error ->`,
        e.toString(),
      );
      setResponseStatus(ctx, 500, 'Unable to load lockTemplate');
    });
};

const generateImageWithoutText = async ctx => {
  const canvas = createCanvas(IMAGE_WITHOUT_TEXT.WIDTH, IMAGE_WITHOUT_TEXT.HEIGHT);
  const canvasContext = canvas.getContext('2d');
  const { font, fontSize, message, color } = ctx.request.query;
  const [firstLine, secondLine] = message.split(LINE_END);

  canvasContext.font = `${fontSize}px ${font}`;
  canvasContext.fillStyle = color;
  canvasContext.textAlign = 'center';
  const firstLineHeight = IMAGE_WITHOUT_TEXT.HEIGHT / 2 + TEXT.LINE_HEIGHT_WITHOUT_IMAGE;
  const secondLineHeight = IMAGE_WITHOUT_TEXT.HEIGHT / 2 + 2 * TEXT.LINE_HEIGHT_WITHOUT_IMAGE;

  canvasContext.fillText(firstLine, IMAGE_WITHOUT_TEXT.WIDTH / 2 + 15, firstLineHeight);
  canvasContext.fillText(secondLine, IMAGE_WITHOUT_TEXT.WIDTH / 2 + 15, secondLineHeight);
  ctx.body = canvas.toBuffer(MIME_TYPES.PNG, {});
  setResponseStatus(ctx, 200);
};

/**
 * Validate query params respect the schema
 * @param ctx {Object} - koa request context
 * @param schema {Object} - Joi module schema
 * @return {Boolean}
 * */

const validateSchema = (ctx, schema) => {
  const { query } = ctx.request;

  const result = Joi.validate(query, Joi.object().keys(schema));
  return result.error === null;
};

exports.generateLockWithText = async ctx => {
  const schema = {
    templateId: Joi.number()
      .integer()
      .positive()
      .required(),
    message: Joi.string().max(40),
    font: Joi.string(),
    fontSize: Joi.number(),
    color: Joi.string(),
  };

  return validateSchema(ctx, schema)
    ? generateImage(ctx)
    : setResponseStatus(ctx, 400, 'Client error, invalid parameters');
};

exports.generateTransparentText = async ctx => {
  const schema = {
    message: Joi.string().max(40),
    font: Joi.string(),
    fontSize: Joi.number(),
    color: Joi.string(),
  };
  return validateSchema(ctx, schema)
    ? generateImageWithoutText(ctx)
    : setResponseStatus(ctx, 400, 'Client error, invalid parameters');
};
