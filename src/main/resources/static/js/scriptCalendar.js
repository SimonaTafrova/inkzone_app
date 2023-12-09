let nav = 0;
let clicked = null;

const weekdays = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday','Sunday'];
const body = document.getElementById("body");
const calendar = document.querySelector(".calendar"),
  date = document.querySelector(".date"),
  daysContainer = document.querySelector(".days"),
  prev = document.querySelector(".prev"),
  next = document.querySelector(".next"),
  todayBtn = document.querySelector(".today-btn"),
  gotoBtn = document.querySelector(".goto-btn"),
  dateInput = document.querySelector(".date-input"),
  eventDay = document.querySelector(".event-day"),
  eventDate = document.querySelector(".event-date"),
  eventsContainer = document.querySelector(".events"),
  addEventBtn = document.querySelector(".add-event"),
  addEventWrapper = document.querySelector(".add-event-wrapper "),
  addEventCloseBtn = document.querySelector(".close "),
  addEventTitle = document.querySelector(".event-name "),
  addEventFrom = document.querySelector(".event-time-from "),
  addEventSubmit = document.querySelector(".add-event-btn ");
  currentUserId = document.getElementById("userId").value;
  topDate = document.getElementById('topdate');
  const addEventForm = document.getElementById("addEventForm");
const csrfHeaderName = document.head.querySelector('[name="_csrf_header"]').content;
const csrfHeaderValue = document.head.querySelector('[name="_csrf"]').content;


  let loggedUser = document.getElementById("loggedUser").value;
  let dt = new Date();
  let month = dt.getMonth();
  let year = dt.getFullYear();
  let activeDay;

  const months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];

  let eventsArr = [];
getEvents();



  function load(){

    const day = dt.getDate();
    const lastDay = new Date(year, month + 1, 0);
    const nextDays = 7 - lastDay.getDay();

    const firstDayOfMonth = new Date(year, month, 1);
    const daysInMonth = new Date(year, month + 1, 0).getDate();

    const dateString = firstDayOfMonth.toLocaleDateString('en-uk', {
      weekday: 'long',
      year: 'numeric',
      month: 'numeric',
      day: 'numeric',
    });
    const paddingDays = weekdays.indexOf(dateString.split(', ')[0]);

    topDate.innerText =  `${months[month]} ${year}`;

    daysContainer.innerHTML='';


    for(let i = 1; i <= paddingDays + daysInMonth; i++) {
      const daySquare = document.createElement('div');

      daySquare.classList.add('day');

      const dayString = `${month + 1}/${i - paddingDays}/${year}`;

      if (i > paddingDays) {
        daySquare.innerText = i - paddingDays;

        //const eventForDay = events.find(e => e.date === dayString);

        if (i - paddingDays === day && month === dt.getMonth() && year === dt.getFullYear()) {
          daySquare.classList.remove('day');
          daySquare.classList.add("day", "today", "active");
          daySquare.addEventListener("click", () => {
            getActiveDay(i - paddingDays);
            updateEvents(i-paddingDays);
          })
          daySquare.click();

        }
      }


      if (
        i === new Date().getDate() &&
        year === new Date().getFullYear() &&
        month === new Date().getMonth()
      ) {
        activeDay = i;

      }

      daySquare.addEventListener("click", () => {
        getActiveDay(i - paddingDays);

      });


      daysContainer.appendChild(daySquare);
      addListner();





  }



  for (let j = 1; j <= nextDays; j++) {
    const daySquare = document.createElement('div');
    daySquare.classList.add("day","next-date");
    daysContainer.appendChild(daySquare);

  }
}


function initButtons() {

    prev.addEventListener('click', () => {
        month--;
        if (month < 0) {
            month = 11;
            year--;
  }
  load();
    });

    next.addEventListener('click', () => {
        month++;
        if (month > 11) {
          month = 0;
          year++;
        }
        load();
    });


  }





  function getActiveDay(date) {
    const day = new Date(year, month, date);
    const dayName = day.toString().split(" ")[0];
    eventDay.innerHTML = dayName;
    eventDate.innerHTML = date + " " + months[month] + " " + year;
  }




function getEvents() {

  //check if events are already saved in local storage then return event else nothing
  fetch(`http://localhost:8080/api/calendar`,{
    headers: {
      "Accept": "application/json"
    }
  }).then(res => res.json())
      .then(data => {
        for(let item of data){

          eventsArr.push(item);

        }
        load();

      })






}

  function addListner() {
    const days = document.querySelectorAll(".day");
    days.forEach((day) => {
      day.addEventListener("click", (e) => {
        updateEvents(Number(e.target.innerHTML));
        activeDay = Number(e.target.innerHTML);
        //remove active
        days.forEach((day) => {
          day.classList.remove("active");
        });
        //if clicked prev-date or next-date switch to that month
        if (e.target.classList.contains("prev-date")) {
          prevMonth();
          //add active to clicked day afte month is change
          setTimeout(() => {
            //add active where no prev-date or next-date
            const days = document.querySelectorAll(".day");
            days.forEach((day) => {
              if (
                !day.classList.contains("prev-date") &&
                day.innerHTML === e.target.innerHTML
              ) {
                day.classList.add("active");
              }
            });
          }, 100);
        } else if (e.target.classList.contains("next-date")) {
          nextMonth();
          //add active to clicked day afte month is changed
          setTimeout(() => {
            const days = document.querySelectorAll(".day");
            days.forEach((day) => {
              if (
                !day.classList.contains("next-date") &&
                day.innerHTML === e.target.innerHTML
              ) {
                day.classList.add("active");
              }
            });
          }, 100);
        } else {
          e.target.classList.add("active");
        }
      });
    });
  }

  todayBtn.addEventListener("click", () => {
    today = new Date();
    month = today.getMonth();
    year = today.getFullYear();
    load();
  });





  function updateEvents(date) {
    let events = "";



      eventsArr.forEach((event) => {
        if (
            date === event.day &&
            month + 1 === event.month &&
            year === event.year
        ) {
          event.events.forEach((event) => {
            events += `<div class="event">
              <div class="title" style="text-decoration-line: underline">
                <i class="fas fa-circle"></i>
                <h2 class="event-title">${event.userName}</h2>
              </div>
              <div class="title">
                <i class="fas fa-circle"></i>
                <h3 class="event-title">${event.title}</h3>
              </div>
              <div class="event-time">
                <span class="event-time">${event.time}</span>
              </div>
          </div>`;
          });
        }
      });




    if (events === "") {
      events = `<div class="no-event">
              <h3>No Events</h3>
          </div>`;
    }
    eventsContainer.innerHTML = events;

  }



  dateInput.addEventListener("input", (e) => {
    dateInput.value = dateInput.value.replace(/[^0-9/]/g, "");
    if (dateInput.value.length === 2) {
      dateInput.value += "/";
    }
    if (dateInput.value.length > 7) {
      dateInput.value = dateInput.value.slice(0, 7);
    }
    if (e.inputType === "deleteContentBackward") {
      if (dateInput.value.length === 3) {
        dateInput.value = dateInput.value.slice(0, 2);
      }
    }
  });

  gotoBtn.addEventListener("click", gotoDate);


  function gotoDate() {

    const dateArr = dateInput.value.split("/");
    if (dateArr.length === 2) {
      if (dateArr[0] > 0 && dateArr[0] < 13 && dateArr[1].length === 4) {
        month = dateArr[0] - 1;
        year = dateArr[1];
        load();
        return;
      }
    }
    alert("Invalid Date");
  }

  addEventBtn.addEventListener("click", () => {
    addEventWrapper.classList.toggle("active");
  });

  addEventCloseBtn.addEventListener("click", () => {
    addEventWrapper.classList.remove("active");
  });

  document.addEventListener("click", (e) => {
    if (e.target !== addEventBtn && !addEventWrapper.contains(e.target)) {
      addEventWrapper.classList.remove("active");
    }
  });

  //allow 50 chars in eventtitle
  addEventTitle.addEventListener("input", (e) => {
    addEventTitle.value = addEventTitle.value.slice(0, 60);
  });


  function defineProperty() {
    var osccred = document.createElement("div");
    osccred.innerHTML =
      "A Project By <a href='https://www.youtube.com/channel/UCiUtBDVaSmMGKxg1HYeK-BQ' target=_blank>Open Source Coding</a>";
    osccred.style.position = "absolute";
    osccred.style.bottom = "0";
    osccred.style.right = "0";
    osccred.style.fontSize = "10px";
    osccred.style.color = "#ccc";
    osccred.style.fontFamily = "sans-serif";
    osccred.style.padding = "5px";
    osccred.style.background = "#fff";
    osccred.style.borderTopLeftRadius = "5px";
    osccred.style.borderBottomRightRadius = "5px";
    osccred.style.boxShadow = "0 0 5px #ccc";
    document.body.appendChild(osccred);
  }




  addEventFrom.addEventListener("input", (e) => {
    addEventFrom.value = addEventFrom.value.replace(/[^0-9:]/g, "");
    if (addEventFrom.value.length === 2) {
      addEventFrom.value += ":";
    }
    if (addEventFrom.value.length > 5) {
      addEventFrom.value = addEventFrom.value.slice(0, 5);
    }
  });



  //function to add event to eventsArr
  addEventSubmit.addEventListener("click", async () => {
    const eventTitle = addEventTitle.value;
    const eventTimeFrom = addEventFrom.value;
    if (eventTitle === "" || eventTimeFrom === "") {
      alert("Please fill all the fields");
      return;
    }

    //check correct time format 24 hour
    const timeFromArr = eventTimeFrom.split(":");
    if (timeFromArr.length !== 2 || timeFromArr[0] > 23 || timeFromArr[1] > 59) {
      alert("Invalid Time Format");
      return;
    }

    const timeFrom = convertTime(eventTimeFrom);


    //check if event is already added
    let eventExist = false;
    eventsArr.forEach((event) => {
      if (
          event.day === activeDay &&
          event.month === month + 1 &&
          event.year === year
      ) {
        event.events.forEach((event) => {
          if (event.title === eventTitle) {
            eventExist = true;
          }
        });
      }
    });
    if (eventExist) {
      alert("Event already added");
      return;
    }
    const newEvent = {
      title: eventTitle,
      time: timeFrom,
      userName: loggedUser
    };


    let calendarDay = {
      day: activeDay,
      month: month + 1,
      year: year,
      events: [newEvent],
    }


    let eventAdded = false;
    if (eventsArr.length > 0) {
      eventsArr.forEach((item) => {
        if (
            item.day === activeDay &&
            item.month === month + 1 &&
            item.year === year
        ) {
          item.events.push(newEvent);
          eventAdded = true;
        }
      });
    }

    if (!eventAdded) {
      eventsArr.push({
        day: activeDay,
        month: month + 1,
        year: year,
        events: [newEvent],
      });
    }

    let url = `http://localhost:8080/api/calendar/add`
    let method = "POST";
    try {
      await postNewEvent({url, calendarDay, method});
      console.log(currentUserFirstName);
      updateEvents(activeDay)




    } catch (error) {
      console.log(error)






    }










      addEventWrapper.classList.remove("active");
      addEventTitle.value = "";
      addEventFrom.value = "";
      updateEvents(activeDay);
      //select active day and add event class if not added
      const activeDayEl = document.querySelector(".day.active");
      if (!activeDayEl.classList.contains("event")) {
        activeDayEl.classList.add("event");
      }
    }
  )


async function postNewEvent({url, calendarDay, method}) {
  const fetchOptions = {
    method: method,
    headers: {
      [csrfHeaderName] : csrfHeaderValue,
      "Content-Type" : "application/json",
      "Accept" :"application/json"
    },
    body: JSON.stringify(calendarDay)
  }

  const response = await fetch(url, fetchOptions);

  if (!response.ok) {
    const errorMessage = await response.text();
    throw new Error(errorMessage);
  }

  return response.json();

}


    function convertTime(time) {
      //convert time to 24 hour format
      let timeArr = time.split(":");
      let timeHour = timeArr[0];
      let timeMin = timeArr[1];
      time = timeHour + ":" + timeMin;
      return time;
    }

function deleteEvent(eventId, dayId) {
  fetch(`http://localhost:8080/api/calendar/${dayId}/${eventId}`, {
        method: "DELETE",
        headers: {
          [csrfHeaderName]: csrfHeaderValue,
          "Content-Type": "application/json",
          "Accept": "application/json"
        }
      }
  ).then(() => { console.log("removed");})
      .catch(error  => {console.error(error)})



}


    eventsContainer.addEventListener("click", (e) => {


      if (e.target.classList.contains("event")) {
        if (confirm("Are you sure you want to delete this event?")) {
          const eventTitle = e.target.children[1].children[1].innerHTML;

          eventsArr.forEach((event) => {
            if (
                event.day === activeDay &&
                event.month === month + 1 &&
                event.year === year
            ) {
              event.events.forEach((item, index) => {


                if (item.title === eventTitle) {
                  if(item.creatorId !== currentUserId){
                    alert("You are not allowed to delete events you did not created!")
                    return;
                  }
                  deleteEvent(item.id, event.id)
                  event.events.splice(index, 1);
                  deleteEvent(item.id, event.id);
                }
              });

              //if no events left in a day then remove that day from eventsArr
              if (event.events.length === 0) {
                eventsArr.splice(eventsArr.indexOf(event), 1);
                //remove event class from day
                const activeDayEl = document.querySelector(".day.active");
                if (activeDayEl.classList.contains("event")) {
                  activeDayEl.classList.remove("event");
                }
              }
            }
          });
          updateEvents(activeDay);
        }
      }
    });



async function postEventDataAsJson({url, newEvent, method}) {



  const formDataAsJSONString = JSON.stringify(newEvent);


  const fetchOptions = {
    method: method,
    headers: {
      [csrfHeaderName] : csrfHeaderValue,
      "Content-Type" : "application/json",
      "Accept" :"application/json"
    },
    body: formDataAsJSONString
  }



  const response = await fetch(url, fetchOptions);

  if (!response.ok) {
    const errorMessage = await response.text();
    throw new Error(errorMessage);
  }

  return response.json();
}


    initButtons();


    load();









