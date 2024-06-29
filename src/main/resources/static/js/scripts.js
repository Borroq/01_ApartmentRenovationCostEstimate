// src/main/resources/static/js/scripts.js
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