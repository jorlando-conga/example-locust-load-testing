const setPadding = (valueIn: string | number, length: number) => {
    let value: string = `${valueIn}`;
    while (value.length < length) {
        value = "0" + value;
    }
    return value;
}
export const toISOString = (date: Date) => {
    return `${date.getUTCFullYear()}-${setPadding((date.getUTCMonth()+1), 2)}-${setPadding(date.getUTCDate(), 2)}`
        + `T${setPadding(date.getUTCHours(), 2)}:${setPadding(date.getUTCMinutes(), 2)}:${setPadding(date.getUTCSeconds(), 2)}`;
}

export const toFormattedString = (date: Date) => {
    return `${setPadding((date.getUTCMonth()+1), 2)}/${setPadding(date.getUTCDate(), 2)}/${date.getUTCFullYear()}`;
}

export function dateToDateString(date: Date | null, formatOptions = undefined) {
    if (!date) {
        return '';
    }
    return date.toLocaleDateString("en-US", formatOptions || {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}