const { createCanvas, loadImage } = require('canvas');
const Joi = require('joi');
const { MIME_TYPES, IMAGE_BASE_DIMENSIONS, LINE_END, HTTP_HEADERS, API_URL } = require('./constants');

const setResponseStatus = (ctx, status, message) => {
  ctx.status = status;


  let errorResponseBody = {
    status: 'ERROR',
    errorCode: 'IG-0',
    message: message || "Generic error",
  };

  switch (status) {
    case 200:
      ctx.set(HTTP_HEADERS.CONTENT_TYPE, MIME_TYPES.PNG);
      return;
    case 500:
      ctx.set(HTTP_HEADERS.CONTENT_TYPE, MIME_TYPES.JSON);
      errorResponseBody = {...errorResponseBody, errorCode: "IG-1"};
      break;
    case 400:
      ctx.set(HTTP_HEADERS.CONTENT_TYPE, MIME_TYPES.JSON);
      errorResponseBody = {...errorResponseBody, errorCode: "IG-2"};
      break;
    default:
      ctx.set(HTTP_HEADERS.CONTENT_TYPE, MIME_TYPES.JSON);
      ctx.body = JSON.stringify(errorResponseBody);
  }
  ctx.body = JSON.stringify(errorResponseBody);
};

const generateImage = async ctx => {

    const {templateId} = ctx.query;
  const imageURL = API_URL.replace("{LOCK_ID}", ctx.query.templateId);
  await loadImage(imageURL, {})
    .then(image => {
      const canvas = createCanvas(
          IMAGE_BASE_DIMENSIONS.WIDTH,
          IMAGE_BASE_DIMENSIONS.HEIGHT,
      );
      const canvasContext = canvas.getContext('2d');
      const { font, fontSize, message, color } = ctx.request.query;
      const [firstLine, secondLine] = message.split(LINE_END);


      //TODO Validate font and register new fonts

      canvasContext.font = `${fontSize}px ${font}`;
      canvasContext.fillStyle = color;
      canvasContext.drawImage(
          image,
          50,
          0,
          IMAGE_BASE_DIMENSIONS.WIDTH,
          IMAGE_BASE_DIMENSIONS.HEIGHT,
      );
      canvasContext.textAlign = 'center';
      canvasContext.fillText(firstLine, 1020, 750);
      canvasContext.fillText(secondLine, 1020, 830);
      ctx.body = canvas.toBuffer(MIME_TYPES.PNG);
      setResponseStatus(ctx, 200);
    })
    .catch(e => {
      ctx.log.error(
        `Unable to load image with id ${templateId} with error ->`,
        e.toString(),
      );
      setResponseStatus(ctx, 500, "Unable to load lockTemplate")
    });

};

const validateSchema = ctx => {
  const { query } = ctx.request;

  const schema = Joi.object().keys({
      templateId: Joi.number()
      .integer()
      .positive()
      .required(),
    message: Joi.string().max(40),
    font: Joi.string(),
    fontSize: Joi.number(),
    color: Joi.string(),
  });

  const result = Joi.validate(query, schema);
  return result.error === null;
};

exports.generateLockWithText = async ctx => {
  if (!validateSchema(ctx)) {
    return setResponseStatus(ctx, 400, 'Client error, invalid parameters');
  }

  return generateImage(ctx);
};
