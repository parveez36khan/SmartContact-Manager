console.log("this is js lang")
function closeSidebar() {
    var sidebar = document.querySelector('.sidebar');
    var content = document.querySelector('.content');
    
    if (sidebar.style.display === 'none' || sidebar.style.display === '') {
        // Sidebar is currently closed, so open it
        sidebar.style.display = 'block';
        content.style.marginLeft = '250px';
        content.style.paddingLeft = '0';
         // Adjust the width of the sidebar as needed
    } else {
        // Sidebar is currently open, so close it
        sidebar.style.display = 'none';
        content.style.marginLeft = '0';
        
    }
}
var imgElement = document.getElementById("imageElement");

if (!imgElement.src || imgElement.src === '') {
    // If the src attribute is missing or empty, set the default image
    imgElement.src = "static/image/contact1.jpg";
}  
   function confirmDelete(contactId) {
    swal({
        title: "Are you sure?",
        text: "Once deleted, you will not be able to recover this contact!",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
    .then((willDelete) => {
        if (willDelete) {
            // If the user confirms, redirect to the delete URL
            window.location.href = "/myuser/delete/" + contactId;
        } else {
            swal("Your contact is safe!");
        }
    });
    
    // Prevent the default action of the anchor tag
    return false;
}
        // Automatically trigger the confirmation on page load
   
function confirmAndUpdate(contactId) {
    swal({
        title: "Are you sure?",
        text: "Once updated, the changes cannot be undone!",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
    .then((willUpdate) => {
        if (willUpdate) {
            // If the user confirms, submit the form
            document.getElementById('updateForm').submit();
        } else {
            swal("Update canceled!");
        }
    });
}
 window.addEventListener('scroll', function() {
        var radialLine = document.getElementById('radialLine');
        if (window.scrollY > 0) {
            radialLine.classList.add('transparent');
        } else {
            radialLine.classList.remove('transparent');
        }
    });