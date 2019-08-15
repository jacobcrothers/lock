const { createCanvas, loadImage } = require('canvas');
const Joi = require('joi');

const MIME_TYPES = {
  PNG: 'image/png',
  JSON: 'application/json',
};

const setResponseStatus = (ctx, status, message) => {
  ctx.status = status;
  if (status === 200) {
    ctx.set('Content-Type', MIME_TYPES.PNG);
  } else {
    ctx.set('Content-Type', MIME_TYPES.JSON);
    ctx.body = JSON.stringify({
      status: 'error',
      errorCode: '1',
      message: message,
    });
  }

  ctx.log.info('Request Successful');
};

const generateImage = async ctx => {
  const canvas = createCanvas(1920, 1382);
  const canvasContext = canvas.getContext('2d');
  const id = 12;
  const {lockId, font, fontSize, firstLine, secondLine, color } = ctx.request.query;
  canvasContext.font = `${fontSize}px ${font}`;

  await loadImage(`https://localhost:6060/api/v0/download/file/${lockId}`, {})
    .then(image => {
        canvasContext.fillStyle  = color;
      canvasContext.drawImage(image, 50, 0, 1920, 1382);
      canvasContext.textAlign = "center";
      canvasContext.fillText(firstLine, 1020, 750 );
      canvasContext.fillText(secondLine, 1020, 830);
      ctx.body = canvas.toBuffer(MIME_TYPES.PNG);
      setResponseStatus(ctx, 200);
    })
    .catch(e => {
      ctx.log.info(
        `Unable to load image with id ${id} with error ->`,
        e.toString(),
      );
    });
};


const validateSchema = ctx => {
  const { params } = ctx.request;

  const schema = Joi.object().keys({
    lockId: Joi.number()
      .integer()
      .positive()
      .required(),
    firstLine: Joi.string().max(15),
    secondLine: Joi.string().max(15),
    font: Joi.string(),
    fontSize: Joi.number(),
    color: Joi.string(),
  });

  const result = Joi.validate(params, schema);
  return result.error === null;
};



exports.generateLockWithText = async ctx => {
  if (!validateSchema(ctx)) {
    return setResponseStatus(ctx, 400, 'Client error, invalid parameters');
  }

  return generateImage(ctx);
};
