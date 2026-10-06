-- Demo customers. PINs: ACC1001=1234, ACC1002=4321, ACC1003=1111, ACC1004=2222
INSERT INTO accounts (account_number, holder_name, balance, currency, pin_hash) VALUES
    ('ACC1001', 'Priya Sharma',  250000.00, 'INR', '81dc9bdb52d04dc20036dbd8313ed055'),
    ('ACC1002', 'Arjun Mehta',   120000.00, 'INR', 'd93591bdf7860e1e4ee2fca799911215'),
    ('ACC1003', 'Fatima Khan',    98000.00, 'INR', 'b59c67bf196a4758191e42f76670ceba'),
    ('ACC1004', 'Daniel Thomas',   5000.00, 'USD', '934b535800b1cba8f96a5d72f72f1611');

INSERT INTO transfers (from_account, to_account, amount, fee, reference, note) VALUES
    ('ACC1001', 'ACC1002', 1500.00, 0.00, 'NB100001', 'Dinner split'),
    ('ACC1002', 'ACC1003', 8000.00, 0.00, 'NB100002', 'Rent share'),
    ('ACC1003', 'ACC1001',  250.00, 0.00, 'NB100003', 'Books');
