const fs = require('fs');
const axios = require('axios');

async function loadConfigFromCloud() {
    const url = 'https://raw.githubusercontent.com/aakashkumar1980/apps-configs/main/SERVER-PORTS.json';
    try {
        const response = await axios.get(url);
        return response.data[0];
    } catch (error) {
        throw new Error(`Error fetching config from GitHub: ${error.message}`);
    }
}

async function writeConfigFiles() {
    try {
        const config = await loadConfigFromCloud();
        const vitePort = config.APPS_TEMPLATES.UI_REACT_PORT;
        const backendPort = config.APPS_TEMPLATES.BACKEND_NODE_PORT;

        // Write Vite config
        const viteConfigContent = `export default { server: { port: ${vitePort} } };`;
        fs.appendFileSync('vite.config.js', viteConfigContent);

        // Write .env file
        const envContent = `VITE_BACKEND_PORT=${backendPort}`;
        fs.appendFileSync('.env', envContent);
    } catch (error) {
        console.error(error.message);
        process.exit(1);
    }
}
writeConfigFiles();
