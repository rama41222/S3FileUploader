export default function ({$axios, redirect}) {
    $axios.onRequest(config => {
        config.headers['Access-Control-Allow-Origin'] = "*";
    })
}
