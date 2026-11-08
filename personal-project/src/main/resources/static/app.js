'use strict';

const $ = (selector) => document.querySelector(selector);
const currency = new Intl.NumberFormat('ko-KR');
let viewRequest = 0;

function feedback(message, error = false) {
  const element = $('#feedback');
  element.textContent = message;
  element.classList.toggle('error', error);
  element.hidden = !message;
}

async function request(url, options) {
  let response;
  try {
    response = await fetch(url, options);
  } catch {
    throw new Error('서버에 연결할 수 없어요. 연결 상태를 확인하고 다시 시도해 주세요.');
  }
  const data = await response.json().catch(() => null);
  if (!response.ok) {
    throw new Error(data?.errors?.join('\n') || `요청을 처리하지 못했어요. (HTTP ${response.status})`);
  }
  return data;
}

function renderItems(items) {
  const container = $('#items');
  container.replaceChildren();
  $('#count').textContent = items.length;
  if (items.length === 0) {
    const empty = document.createElement('div');
    empty.className = 'empty';
    const icon = document.createElement('div');
    icon.className = 'empty-icon';
    icon.textContent = '◇';
    const title = document.createElement('h3');
    title.textContent = '아직 이곳은 비어 있어요';
    const description = document.createElement('p');
    description.textContent = '새 아이템을 등록하거나 다른 조건으로 찾아보세요.';
    empty.append(icon, title, description);
    container.append(empty);
    return;
  }
  for (const item of items) {
    const card = document.createElement('article');
    card.className = 'item-card';
    const number = document.createElement('span');
    number.className = 'item-number';
    number.textContent = `NO. ${String(item.id).padStart(3, '0')}`;
    const dot = document.createElement('span');
    dot.className = 'card-dot';
    dot.setAttribute('aria-hidden', 'true');
    dot.textContent = '✳';
    const name = document.createElement('h3');
    name.textContent = item.name;
    const price = document.createElement('p');
    price.className = 'item-price';
    price.textContent = currency.format(item.price);
    const unit = document.createElement('small');
    unit.textContent = '원';
    price.append(unit);
    card.append(number, dot, name, price);
    container.append(card);
  }
}

async function loadView(url = '/item', description = '내가 모아둔 모든 아이템을 확인하세요.') {
  const currentRequest = ++viewRequest;
  $('#items').replaceChildren();
  const loading = document.createElement('p');
  loading.className = 'loading';
  loading.textContent = '아이템을 불러오는 중이에요…';
  $('#items').append(loading);
  $('#count').textContent = '…';
  $('#view-description').textContent = description;
  $('#show-all').classList.toggle('active', url === '/item');
  try {
    const data = await request(url);
    if (currentRequest !== viewRequest) return;
    renderItems(Array.isArray(data) ? data : [data]);
  } catch (error) {
    if (currentRequest !== viewRequest) return;
    renderItems([]);
    feedback(error.message, true);
  }
}

function integerValue(input, minimum = 0, maximum = 2147483647) {
  const value = Number(input.value);
  if (input.value === '' || !Number.isSafeInteger(value) || value < minimum || value > maximum) {
    throw new Error(`${minimum.toLocaleString('ko-KR')}~${maximum.toLocaleString('ko-KR')} 사이의 정수를 입력해 주세요.`);
  }
  return value;
}

$('#add-form').addEventListener('submit', async (event) => {
  event.preventDefault();
  const button = event.currentTarget.querySelector('button');
  feedback('');
  try {
    const name = $('#name').value.trim();
    if (!name || name.length > 20) throw new Error('아이템 이름을 1~20자로 입력해 주세요.');
    const price = integerValue($('#price'));
    button.disabled = true;
    await request('/item', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, price })
    });
    $('#add-form').reset();
    feedback('새 아이템을 컬렉션에 추가했어요.');
    await loadView();
  } catch (error) {
    feedback(error.message, true);
  } finally {
    button.disabled = false;
  }
});

$('#budget-form').addEventListener('submit', (event) => {
  event.preventDefault();
  feedback('');
  try {
    const budget = integerValue($('#budget'));
    loadView(`/item/recommendations?budget=${budget}`, `${currency.format(budget)}원 이하의 아이템 · 낮은 가격순 · 최대 3개`);
  } catch (error) { feedback(error.message, true); }
});

$('#lookup-form').addEventListener('submit', (event) => {
  event.preventDefault();
  feedback('');
  try {
    const id = integerValue($('#item-id'), 1, Number.MAX_SAFE_INTEGER);
    loadView(`/item/${id}`, `아이템 ID ${id}의 조회 결과입니다.`);
  } catch (error) { feedback(error.message, true); }
});

for (const selector of ['#show-all', '#refresh']) {
  $(selector).addEventListener('click', () => { feedback(''); loadView(); });
}
loadView();
