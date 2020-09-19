import http from 'k6/http';
import {check, sleep} from 'k6';
import {randomIntBetween} from 'https://jslib.k6.io/k6-utils/1.0.0/index.js';

export let baseUrl = 'http://localhost';

export let options = {
  stages: [
    {duration: '20s', target: 200},
    {duration: '20s', target: 200},
    {duration: '20s', target: 0}
  ]
};

export default () => {
  let num = randomIntBetween(1, 1000000);
  let text = Math.random().toString(36).substring(2) + Date.now().toString(36);

  let result = http.post(`${baseUrl}/models`, JSON.stringify({
    num: num,
    text: text
  }), {
    headers: {
      'Content-Type': 'application/json',
    },
  });

  check(result, {
    'status must be 201': (res) => res.status === 201,
    'num must be the same': (res) => JSON.parse(res.body).num === num,
    'text must be the same': (res) => JSON.parse(res.body).text === text
  });

  sleep(0.1);
}