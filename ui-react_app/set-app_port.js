const axios = require('axios');
const propertiesReader = require('properties-reader');

async function loadConfigFromCloud() {
  try {
    const url = 'https://raw.githubusercontent.com/aakashkumar1980/apps-configs/main/SERVER-PORTS.json';
    const response = await axios.get(url);

    return response.data[0];
  } catch (error) {
    throw new Error(`Error fetching config from GitHub: ${error.message}`);
  }
}

async function getPort() {
  const config = await loadConfigFromCloud();
  return config.APPS_TEMPLATES.UI_REACT_PORT;
}
module.exports = getPort;
