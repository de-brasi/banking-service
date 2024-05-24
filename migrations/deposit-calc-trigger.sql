CREATE OR REPLACE FUNCTION calculate_max_deposit()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.max_deposit := ROUND(NEW.deposit * 2.07, 2); -- 207%
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_calculate_max_deposit
    BEFORE INSERT ON bank_accounts
    FOR EACH ROW
EXECUTE FUNCTION calculate_max_deposit();