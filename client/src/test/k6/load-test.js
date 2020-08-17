import http from 'k6/http';
import {check, sleep} from 'k6';

export let options = {
  stages: [
    {duration: '20s', target: 200},
    {duration: '20s', target: 200},
    {duration: '20s', target: 0}
  ]
};

export default () => {
  let requestId = Math.random().toString(36).substring(2) + Date.now().toString(36);

  let result = http.post('http://localhost/demo', JSON.stringify({
    requestId: requestId,
    request: 'hello'
  }), {
    headers: {
      'Content-Type': 'application/json',
    },
  });

  check(result, {
    'status must be 200': (res) => res.status === 200,
    'requestId must be the same:': (res) => JSON.parse(res.body).requestId === requestId,
    'response must be hi': (res) => JSON.parse(res.body).response === 'hi'
  });

  sleep(0.1);
}