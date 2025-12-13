const toDoForm = document.getElementById("todo-form");
const toDoInput = toDoForm.querySelector("input");

const toDoList = document.getElementById("todo-list");

const TODOS_KEY = "todos";

let toDos = [];

function saveToDos() {
    localStorage.setItem(TODOS_KEY, JSON.stringify(toDos));
}

function deleteToDo(e) {
    const li = e.target.parentElement;
    li.remove();

    toDos = toDos.filter((toDoObj) => toDoObj.id !== parseInt(li.id));
    saveToDos()
}

function paintToDo(toDoObj) {
    const li = document.createElement("li");
    li.id = toDoObj.id;

    const span = document.createElement("span");
    span.innerText = toDoObj.text;

    const button = document.createElement("button");
    button.innerText = "❌";
    button.addEventListener("click", deleteToDo);

    li.appendChild(span);
    li.appendChild(button);

    toDoList.appendChild(li);
}

function handleToDoSubmit(e) {
    e.preventDefault();

    const newToDo = toDoInput.value;
    const newToDoObj = {
        id: Date.now(),
        text: newToDo,
    }

    toDos.push(newToDoObj);

    paintToDo(newToDoObj);

    toDoInput.value = "";
    saveToDos();
}

toDoForm.addEventListener("submit", handleToDoSubmit);

const savedToDos = localStorage.getItem(TODOS_KEY);

if (savedToDos) {
    const parsedToDos = JSON.parse(savedToDos);
    toDos = parsedToDos;
    parsedToDos.forEach(paintToDo);
}
