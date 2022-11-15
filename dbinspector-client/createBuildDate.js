const { writeFileSync } = require('fs')
const { join } = require('path')

const TIME_STAMP_PATH = join(__dirname, './src/build.date.json');

const createBuildDate = {
  timestamp: new Date().valueOf()
}

writeFileSync(TIME_STAMP_PATH, JSON.stringify(createBuildDate, null, 2));
