// src/main/resources/static/js/scripts.js
document.addEventListener('DOMContentLoaded', function () {
    console.log("Script loaded"); //sprawdzanie, czy skrypt sie wykonuje
    console.log("DOM w pełni załadowany i przetworzony");

    function deleteElement(elementId, name) {
        if (confirm('Are you sure you want to delete this element?')) {
            fetch('/api/' + name + '/' + elementId, {method: 'DELETE'})
                .then(response => {
                    if (response.ok) {
                        alert('Element successfully deleted');
                        location.reload();
                    }else {
                        alert('Failed to delete element');
                    }
                });
        }
    }

/*//Skrypt wykorzystywany do przetwarzania formularza USER FORM
    document.getElementById('userForm').addEventListener('submit', function(event) {
        event.preventDefault(); // zapobiega domyślnemu wysłaniu formularza

        const formData = new FormData(this);
        const jsonData = {};

        formData.forEach((value, key) => {
            jsonData[key] = value;
        });

        fetch('/api/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonData)
        })
        .then(response => {
            if (response.ok) {
                alert('User successfully created');
                this.reset(); // resetuje formularz
                location.reload();
            } else {
                alert('Failed to create user');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while creating the user');
        });
    });

//Skrypt wykorzystywany do przetwarzania formularza PRODUCT FORM
    document.getElementById('productForm').addEventListener('submit', function(event) {
        event.preventDefault(); // zapobiega domyślnemu wysłaniu formularza

        const formData = new FormData(this);
        const jsonData = {};

        formData.forEach((value, key) => {
            jsonData[key] = value;
        });

        fetch('/api/products', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonData)
        })
        .then(response => {
            if (response.ok) {
                alert('Product successfully created');
                this.reset(); // resetuje formularz
                location.reload();
            } else {
                alert('Failed to create product');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while creating the user');
        });
    });

//Skrypt wykorzystywany do przetwarzania formularza ROOM FORM
    document.getElementById('roomForm').addEventListener('submit', function(event) {
        event.preventDefault(); // zapobiega domyślnemu wysłaniu formularza

        const formData = new FormData(this);
        const jsonData = {};

        formData.forEach((value, key) => {
            jsonData[key] = value;
        });

        fetch('/api/rooms', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonData)
        })
        .then(response => {
            if (response.ok) {
                alert('Room successfully created');
                this.reset(); // resetuje formularz
                location.reload();
            } else {
                alert('Failed to create room');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while creating the user');
        });
    });*/
});

