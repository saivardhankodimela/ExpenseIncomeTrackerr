// Handle form submission for expense
document.getElementById('expense-form').addEventListener('submit', function (event) {
    event.preventDefault();

    const description = document.getElementById('expense-description').value.trim();
    const amount = parseFloat(document.getElementById('expense-amount').value);
    const date = document.getElementById('expense-date').value;
    const category = document.getElementById('expense-category').value;

    if (!description || isNaN(amount) || !date || !category) {
        alert('Please fill out all the fields correctly.');
        return;
    }

    const expenseData = { description, amount, date, category };

    fetch('/api/expense', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(expenseData)
    })
        .then(response => {
            if (!response.ok) throw new Error('Failed to add expense');
            return response.json();
        })
        .then(() => {
            alert('Expense added successfully!');
            document.getElementById('expense-form').reset();
        })
        .catch(error => {
            console.error('Error:', error.message);
            alert('An error occurred while adding the expense. Please try again.');
        });
});

// Handle form submission for income
document.getElementById('income-form').addEventListener('submit', function (event) {
    event.preventDefault();

    const description = document.getElementById('income-description').value.trim();
    const amount = parseFloat(document.getElementById('income-amount').value);
    const date = document.getElementById('income-date').value;

    if (!description || isNaN(amount) || !date) {
        alert('Please fill out all the fields correctly.');
        return;
    }

    const incomeData = { description, amount, date };

    fetch('/api/income', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(incomeData)
    })
        .then(response => {
            if (!response.ok) throw new Error('Failed to add income');
            return response.json();
        })
        .then(() => {
            alert('Income added successfully!');
            document.getElementById('income-form').reset();
        })
        .catch(error => {
            console.error('Error:', error.message);
            alert('An error occurred while adding the income. Please try again.');
        });
});

// Separate fetchProfitLoss function
function fetchProfitLoss() {
    const startDate = document.getElementById('start-date').value;
    const endDate = document.getElementById('end-date').value;

    if (!startDate || !endDate) {
        alert('Please select both start and end dates.');
        return;
    }

    fetch(`/api/profit-loss?startDate=${startDate}&endDate=${endDate}`)
        .then(response => {
            if (!response.ok) throw new Error('Failed to fetch profit/loss');
            return response.text(); // Expecting plain text result from the backend
        })
        .then(data => {
            const resultMessage = document.getElementById('profit-loss-message');
            if (data.includes('Profit')) {
                resultMessage.textContent = data;
                resultMessage.style.color = 'green';
            } else if (data.includes('Loss')) {
                resultMessage.textContent = data;
                resultMessage.style.color = 'red';
            } else {
                resultMessage.textContent = 'Break-even';
                resultMessage.style.color = 'orange';
            }
        })
        .catch(error => {
            console.error('Error:', error.message);
            alert('An error occurred while calculating profit/loss. Please try again.');
        });
}

// Add event listener for profit/loss form
document.getElementById('profit-loss-form').addEventListener('submit', function (event) {
    event.preventDefault();
    fetchProfitLoss();
});
