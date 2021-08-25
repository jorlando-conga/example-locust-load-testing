import {gql} from "@apollo/client";

export const UPSERT_LIST_EVENT = gql`
    mutation upsertListEvent($eventId: ID, $event: UpsertListEvent!) {
        upsertListEvent(eventId: $eventId, event: $event)
    }
`;

export interface UpsertListEvent {
    description: string;
    dueDate: string;
    zipCode: string;
    completed?: boolean | undefined;
}