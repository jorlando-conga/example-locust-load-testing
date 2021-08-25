import {gql} from "@apollo/client";

export const GET_LIST_EVENTS = gql`
    query fetchListEvents($endDate: String!) {
        fetchListEvents(endDate: $endDate) {
            id
            description
            dueDate
            zipCode
            completed
        }
    }
`;

export interface ListEventRecord {
    id: string;
    description: string;
    dueDate: string;
    zipCode: string;
    completed: boolean;
}

export const GET_EVENT_WEATHER = gql`
    query getEventWeather($eventId: ID!) {
        getEventWeather(eventId: $eventId) {
            id
            zipCode
            conditions
            temperature
            windSpeed
            lastUpdatedAt
        }
    }
`;

export interface WeatherResultRecord {
    id: string;
    zipCode: string;
    conditions: string;
    temperature: string;
    lastUpdatedAt: string;
}