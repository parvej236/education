const modalInit = (elemId) => {
  return new BSN.Modal(elemId, { backdrop: true })
}

function showToast(message, success = true) {
  let toast = document.getElementById('toast')

  toast.addEventListener('show.bs.toast', function (event) {
    if(success) {
      toast.classList.remove('bg-danger')
      toast.classList.add('bg-success')
    } else {
      toast.classList.remove('bg-success')
      toast.classList.add('bg-danger')
    }

    let modalBody = toast.querySelector('.toast-body')
    modalBody.innerHTML = `${message}`
  })

  let toastInitial = tostInit(toast)
  toastInitial.show()
}

function toReadableName(str, separator = '_', concat= ' '){
  return str.split(separator).map(function(word){
    return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
  }).join(concat)
}

const getApi = (url) => {
  return fetch(url).then(response => response.json())
}

const postTextApi = (url, data) => {
  return fetch(url,
      {
        method: 'post',
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
      }).then(response => response.text())
}

const getTodayDate = (increment = 0, delimiter = '-') => {
  let today = new Date()
  return convertDateTo(today, increment, delimiter)
}

const convertDateTo = (date, increment = 0, delimiter = '-') => {
  date.setDate(date.getDate() + increment)

  let dd = (date.getDate() + 100).toString().substring(1)
  let mm = (date.getMonth() + 101).toString().substring(1)
  let yyyy = date.getFullYear().toString()

  return [yyyy, mm, dd].join(delimiter)
}

const convertDateToStr = (date, increment = 0, delimiter = '/') => {
  date.setDate(date.getDate() + increment)

  let dd = (date.getDate() + 100).toString().substring(1)
  let mm = (date.getMonth() + 101).toString().substring(1)
  let yyyy = date.getFullYear().toString()

  return [dd, mm, yyyy].join(delimiter)
}

const strToUtcDate = dateStr => {
  let [day, month, year] = dateStr.split("/")
  return new Date(Date.UTC(year, month-1, day))
}

function generateDecimalNumber(value, digit = 2) {
  return parseFloat(value.toFixed(digit))
}

function checkDecimalDigitLength(value) {
  if ((value % 1) != 0) return value.toString().split(".")[1].length
  return 0
}

function processDecimalNumbers(arr, columnList) {
  let result = arr.map(obj => {
    for(let prop of columnList){
      let val = obj[prop]

      obj[prop] = generateDecimalNumber(val, 5)
      obj[prop + 'Display'] = generateDecimalNumber(val)
    }

    return obj
  })

  return result
}