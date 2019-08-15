'use strict';

const controller = require('./lock.controller');

module.exports = Router => {
  const router = new Router();

  router.get('/generateImage', controller.generateLockWithText);

  return router;
};
