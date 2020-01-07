'use strict';

const controller = require('./lock.controller');

module.exports = Router => {
  const router = new Router();

  router.get('/generateImage', controller.generateLockWithText);
  router.get('/generateText', controller.generateTransparentText);

  return router;
};
